package com.junyoung.ha.features.orderbook.datasource

import android.util.Log
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import com.junyoung.ha.features.orderbook.domain.model.OrderInfo
import com.junyoung.ha.features.orderbook.domain.model.OrderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.math.BigDecimal

class OrderBookWebSocketDataSourceImpl: OrderBookDataSource {

    companion object {
        val KEY_TABLE = "table"
        val KEY_ACTION = "action"
        val KEY_SIZE = "size"

        val VALUE_ORDER_BOOK_L2 = "orderBookL2"
        val VALUE_PARTIAL = "partial"
        val VALUE_UPDATE = "update"
        val VALUE_DELETE = "delete"
        val VALUE_INSERT = "insert"
    }

    private val buyOrderInfoMap = HashMap<Price, OrderInfo>()
    private val sellOrderInfoMap = HashMap<Price, OrderInfo>()

    private val orderBookMutableFlow by lazy { MutableStateFlow(OrderBook.EMPTY) }
    private val orderBookWebSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            webSocket.send("{\"op\": \"subscribe\", \"args\": [\"orderBookL2:XBTUSD\"]}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            handleMessage(text)
        }


        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            connect()
        }
    }

    private val orderBookTradesThreadContext = newSingleThreadContext("OrderBookWebSocketDataSourceThread")

    init {
        connect()
    }

    private fun connect() {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("wss://ws.bitmex.com/realtime")
            .build()
        client.newWebSocket(request, orderBookWebSocketListener)
    }

    private fun handleMessage(text: String) {
        val hashMap = HashMap<Price, OrderInfo>()
        hashMap.toSortedMap().map {

        }
        CoroutineScope(orderBookTradesThreadContext).launch {
            val jsonObject = JSONTokener(text).nextValue() as JSONObject
            if (jsonObject.isOrderBook().not()) {
                return@launch
            }

            if (jsonObject.isPartial()) {
                jsonObject.parseData()
                    .forEach { insertOrderInfo(it) }
            } else if (jsonObject.isUpdate()) {
                jsonObject.parseData()
                    .forEach { updateOrderInfo(it) }
            } else if (jsonObject.isDelete()) {
                jsonObject.parseData()
                    .forEach { deleteOrderInfo(it) }
            } else if (jsonObject.isInsert()) {
                jsonObject.parseData()
                    .forEach { insertOrderInfo(it) }
            }

            orderBookMutableFlow.emit(
                OrderBook(
                    buyOrderList = buildOrderList(
                        buyOrderInfoMap.toSortedMap(compareBy<Price> { it }.reversed())
                            .toList()
                            .subList(0, 20)
                            .map { it.second }
                    ),
                    sellOrderList = buildOrderList(
                        sellOrderInfoMap.toSortedMap()
                            .toList()
                            .subList(0, 20)
                            .map { it.second }
                    )
                )
            )
        }
    }

    private fun JSONObject.isOrderBook(): Boolean {
        return has(KEY_TABLE) && getString(KEY_TABLE).equals(VALUE_ORDER_BOOK_L2)
    }

    private fun JSONObject.isPartial(): Boolean {
        return has(KEY_ACTION) && getString(KEY_ACTION).equals(VALUE_PARTIAL)
    }

    private fun JSONObject.isUpdate(): Boolean {
        return has(KEY_ACTION) && getString(KEY_ACTION).equals(VALUE_UPDATE)
    }

    private fun JSONObject.isDelete(): Boolean {
        return has(KEY_ACTION) && getString(KEY_ACTION).equals(VALUE_DELETE)
    }

    private fun JSONObject.isInsert(): Boolean {
        return has(KEY_ACTION) && getString(KEY_ACTION).equals(VALUE_INSERT)
    }

    private fun JSONObject.parseData(): List<OrderInfo> {
        val orderInfoList = mutableListOf<OrderInfo>()
        val jsonArray = getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            orderInfoList.add(jsonObject.toOrderInfo())
        }
        return orderInfoList
    }

    private fun JSONObject.toOrderInfo(): OrderInfo {
        return OrderInfo(
            id = getString("id"),
            orderType = when (getString("side")) {
                "Sell" -> OrderType.SELL
                "Buy" -> OrderType.BUY
                else -> throw Exception("Unknown side")
            },
            quantity = if (has(KEY_SIZE)) BigDecimal(getDouble(KEY_SIZE)) else BigDecimal.ZERO,
            price = Price(getDouble("price"))
        )
    }

    private fun updateOrderInfo(orderInfo: OrderInfo) {
        when (orderInfo.orderType) {
            OrderType.BUY -> {
                if (buyOrderInfoMap.containsKey(orderInfo.price)) {
                    buyOrderInfoMap.replace(orderInfo.price, orderInfo)
                } else {
                    buyOrderInfoMap[orderInfo.price] = orderInfo
                    Log.e("OrderBookWebSocketDataSourceImpl", "updateOrderInfo is Something wrong!!!! $orderInfo")
                }

            }
            OrderType.SELL -> {
                if (sellOrderInfoMap.containsKey(orderInfo.price)) {
                    sellOrderInfoMap.replace(orderInfo.price, orderInfo)
                } else {
                    sellOrderInfoMap[orderInfo.price] = orderInfo
                    Log.e("OrderBookWebSocketDataSourceImpl", "updateOrderInfo is Something wrong!!!! $orderInfo")
                }
            }
        }
    }

    private fun deleteOrderInfo(orderInfo: OrderInfo) {
        when (orderInfo.orderType) {
            OrderType.BUY -> buyOrderInfoMap.remove(orderInfo.price)
            OrderType.SELL -> sellOrderInfoMap.remove(orderInfo.price)
        }
    }

    private fun insertOrderInfo(orderInfo: OrderInfo) {
        when (orderInfo.orderType) {
            OrderType.BUY -> {
                if (buyOrderInfoMap.containsKey(orderInfo.price)) {
                    buyOrderInfoMap.replace(orderInfo.price, orderInfo)
                    Log.e("OrderBookWebSocketDataSourceImpl", "insertOrderInfo is Something wrong!!!! $orderInfo")
                } else {
                    buyOrderInfoMap[orderInfo.price] = orderInfo
                }

            }
            OrderType.SELL -> {
                if (sellOrderInfoMap.containsKey(orderInfo.price)) {
                    sellOrderInfoMap.replace(orderInfo.price, orderInfo)
                    Log.e("OrderBookWebSocketDataSourceImpl", "insertOrderInfo is Something wrong!!!! $orderInfo")
                } else {
                    sellOrderInfoMap[orderInfo.price] = orderInfo
                }
            }
        }
    }

    override suspend fun observeOrderBook(): Flow<OrderBook> {
        return orderBookMutableFlow.asStateFlow()
    }
}