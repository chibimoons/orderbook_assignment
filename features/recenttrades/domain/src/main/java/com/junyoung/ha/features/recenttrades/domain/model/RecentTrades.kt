package com.junyoung.ha.features.recenttrades.domain.model

data class RecentTrades(
    val tradeInfoList: List<TradeInfo> = emptyList()
) {
    companion object {
        val EMPTY = RecentTrades()
    }
}