package com.junyoung.ha.features.recenttrades.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class TradeInfo(
    val tradeType: TradeType,
    val price: Price,
    val quantity: BigDecimal,
    val tradeAt: LocalDateTime
) {
    fun isNew(): Boolean {
        return LocalDateTime.now().isBefore(tradeAt.plus(1000, ChronoUnit.MILLIS))
    }
}