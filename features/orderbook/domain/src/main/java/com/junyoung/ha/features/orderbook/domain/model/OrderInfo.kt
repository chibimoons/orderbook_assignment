package com.junyoung.ha.features.orderbook.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.math.BigDecimal

data class OrderInfo(
    val id: String = "",
    val orderType: OrderType = OrderType.BUY,
    val quantity: BigDecimal = BigDecimal.ZERO,
    val cumulativeQuantity: BigDecimal = BigDecimal.ZERO,
    val price: Price = Price.ZERO,
) {
    companion object {
        val EMPTY = OrderInfo()
    }
}