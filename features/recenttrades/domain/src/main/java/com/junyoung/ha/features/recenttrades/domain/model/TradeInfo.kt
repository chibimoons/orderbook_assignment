package com.junyoung.ha.features.recenttrades.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.math.BigDecimal
import java.time.LocalDateTime

data class TradeInfo(
    val id: String,
    val tradeType: TradeType,
    val price: Price,
    val quantity: BigDecimal,
    val tradeAt: LocalDateTime
) {
    companion object {
        val EMPTY = TradeInfo(
            id = "",
            tradeType = TradeType.BUY,
            price = Price.ZERO,
            quantity = BigDecimal.ZERO,
            tradeAt = LocalDateTime.MIN
        )
    }
}