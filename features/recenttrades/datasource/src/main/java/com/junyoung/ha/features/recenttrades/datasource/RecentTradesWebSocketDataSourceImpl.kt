package com.junyoung.ha.features.recenttrades.datasource

import android.util.Log
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.model.TradeInfo
import com.junyoung.ha.features.recenttrades.domain.model.TradeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private val tradeInfoList: MutableList<TradeInfo> = mutableListOf()
    private val tradeInfoListMutableFlow by lazy { MutableStateFlow(RecentTrades.EMPTY) }
    private val recentTradesWebSocketListener = object: WebSocketListener() {

        private var isConnected: Boolean = false

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            isConnected = true
            webSocket.send("{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("chibimoons", "onMessage text: $text")
            handleMessage(text)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            isConnected = false
            Log.d("chibimoons", "onClosed code:$code reason: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            isConnected = false
            Log.d("chibimoons", "onMessage error: ${t.message} ${response}")
            connect()
        }
    }

    private val recentTradesThreadContext = newSingleThreadContext("RecentTradesWebSocketDataSourceThread")

    init {
        connect()
    }

    private fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("wss://ws.bitmex.com/realtime")
                .build()
            client.newWebSocket(request, recentTradesWebSocketListener)
        }
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
                if (tradeInfoList.size + parsedTradeInfoList.size > 30) {
                    tradeInfoList.dropLast(tradeInfoList.size + parsedTradeInfoList.size - 30)
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
        val tradeInfoList: MutableList<TradeInfo> = mutableListOf()
        val jsonArray = getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            tradeInfoList.add(jsonObject.toTradeInfo())
        }

        return tradeInfoList
    }

    private fun JSONObject.toTradeInfo(): TradeInfo {
        val id = getString("trdMatchID")
        val side = getString("side")
        val price = getDouble("price")
        val size = getLong("size")
        val timestamp = getString("timestamp")
        return TradeInfo(
            id = id,
            tradeType = when (side) {
                "Sell" -> TradeType.SELL
                "Buy" -> TradeType.BUY
                else -> TradeType.UNKNOWN
            },
            price = Price(price),
            quantity = BigDecimal(size),
            tradeAt = timestamp.toLocalDateTime()
        )
    }

    private fun String.toLocalDateTime(): LocalDateTime {
        val zdt = ZonedDateTime.parse(this)
        return zdt.withZoneSameInstant(ZoneId.systemDefault())?.toLocalDateTime()
            ?: throw Exception("")
    }

    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        return tradeInfoListMutableFlow.asStateFlow()
    }
}