package com.junyoung.ha.features.recenttrades.datasource

import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.model.TradeInfo
import com.junyoung.ha.features.recenttrades.domain.model.TradeType
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
import okhttp3.internal.toImmutableList
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class RecentTradesWebSocketDataSourceImpl: RecentTradesDataSource {

    companion object {
        val KEY_TABLE = "table"
        val KEY_ACTION = "action"
        val VALUE_TRADE = "trade"
        val VALUE_INSERT = "insert"
    }

    private var tradeInfoList: MutableList<TradeInfo> = mutableListOf()
    private val tradeInfoListMutableFlow by lazy { MutableStateFlow(RecentTrades.EMPTY) }
    private val recentTradesWebSocketListener = object: WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            webSocket.send("{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}")
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

    private val recentTradesThreadContext = newSingleThreadContext("RecentTradesWebSocketDataSourceThread")

    init {
        connect()
    }

    private fun connect() {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("wss://ws.bitmex.com/realtime")
            .build()
        client.newWebSocket(request, recentTradesWebSocketListener)
    }

    private fun handleMessage(text: String) {
        CoroutineScope(recentTradesThreadContext).launch {
            val jsonObject = JSONTokener(text).nextValue() as JSONObject
            if (jsonObject.isTrade().not()) {
                return@launch
            }

            if (jsonObject.isInsert()) {
                val parsedTradeInfoList = try {
                    jsonObject.parseData()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                    emptyList()
                }
                tradeInfoList.addAll(0, parsedTradeInfoList)
                if (tradeInfoList.size > 30) {
                    tradeInfoList = tradeInfoList.dropLast(tradeInfoList.size - 30).toMutableList()
                }
                tradeInfoListMutableFlow.emit(
                    RecentTrades(
                        newTradeIdSet = parsedTradeInfoList.map { it.id }.toHashSet(),
                        tradeInfoList = tradeInfoList.toImmutableList()
                    )
                )
            }
        }
    }

    private fun JSONObject.isTrade(): Boolean {
        return has(KEY_TABLE) && getString(KEY_TABLE).equals(VALUE_TRADE)
    }

    private fun JSONObject.isInsert(): Boolean {
        return has(KEY_ACTION) && getString(KEY_ACTION).equals(VALUE_INSERT)
    }

    private fun JSONObject.parseData(): List<TradeInfo> {
        val tradeInfoList = mutableListOf<TradeInfo>()
        val jsonArray = getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            tradeInfoList.add(jsonObject.toTradeInfo())
        }

        return tradeInfoList
    }

    private fun JSONObject.toTradeInfo(): TradeInfo {
        return TradeInfo(
            id = getString("trdMatchID"),
            tradeType = when (getString("side")) {
                "Sell" -> TradeType.SELL
                "Buy" -> TradeType.BUY
                else -> throw Exception("Unknown side")
            },
            price = Price(getDouble("price")),
            quantity = BigDecimal(getDouble("size")),
            tradeAt = getString("timestamp").toLocalDateTime()
        )
    }

    private fun String.toLocalDateTime(): LocalDateTime {
        return ZonedDateTime.parse(this)
            .withZoneSameInstant(ZoneId.systemDefault())?.toLocalDateTime() ?: throw Exception("$this parse failed!")
    }

    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        return tradeInfoListMutableFlow.asStateFlow()
    }
}