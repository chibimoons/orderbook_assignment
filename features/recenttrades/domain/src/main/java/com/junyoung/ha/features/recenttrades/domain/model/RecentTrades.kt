package com.junyoung.ha.features.recenttrades.domain.model

data class RecentTrades(
    val newTradeIdSet: HashSet<String> = HashSet(),
    val tradeInfoList: List<TradeInfo> = emptyList()
) {
    companion object {
        val EMPTY = RecentTrades()
    }

    fun isNew(tradeId: String) : Boolean {
        return newTradeIdSet.contains(tradeId)
    }
}