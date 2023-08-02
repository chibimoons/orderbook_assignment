package com.junyoung.ha.features.recenttrades.datasource

import android.util.Log
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.model.TradeInfo
import com.junyoung.ha.features.recenttrades.domain.model.TradeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime

class RecentTradesDataSourceMock: RecentTradesDataSource {

    private val tradeInfoList: MutableList<TradeInfo> = mutableListOf()
    private val tradeInfoListMutableFlow by lazy { MutableStateFlow(RecentTrades.EMPTY) }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Log.d("RecentTradesDataSourceMock", "keep going ${tradeInfoList.size}")
                delay((10..1000).random().toLong())
                if (tradeInfoList.size < 30) {
                    tradeInfoList.add(0, buildDummyTradeInfo(Price(51829.2)))
                } else {
                    tradeInfoList.add(0, buildDummyTradeInfo(tradeInfoList[0].price))
                    tradeInfoList.removeLast()
                }
                tradeInfoListMutableFlow.emit(RecentTrades(tradeInfoList.toList()))
            }
        }
    }

    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        return tradeInfoListMutableFlow.asStateFlow()
    }

    private fun buildDummyTradeInfo(price: Price): TradeInfo {
        return TradeInfo(
            tradeType = if ((1..10).random() % 2 == 0) TradeType.BUY else TradeType.SELL,
            price = price + Price((-10L..10L).random().toDouble() / 10),
            quantity = BigDecimal( (1..10).random().toDouble() / 10000),
            tradeAt = LocalDateTime.now()
        )
    }
}