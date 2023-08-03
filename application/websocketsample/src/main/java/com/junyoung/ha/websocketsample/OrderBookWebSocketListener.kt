package com.junyoung.ha.websocketsample

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class OrderBookWebSocketListener: WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("chibimoons", "onOpen response: ${response.message}")
        webSocket.send("{\"op\": \"subscribe\", \"args\": [\"orderBookL2:XBTUSD\"]}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("chibimoons", "onMessage text: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d("chibimoons", "onMessage bytes: $bytes")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("chibimoons", "onMessage error: ${t.message}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("chibimoons", "onClosing code:$code reason: $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("chibimoons", "onClosed code:$code reason: $reason")
    }
}