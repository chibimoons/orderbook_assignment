package com.junyoung.ha.features.orderbook.domain.model

import java.math.BigDecimal

data class OrderList(
    val orderInfoList: List<OrderInfo> = emptyList(),
    val maxCumulativeQuantity: BigDecimal = BigDecimal.ZERO,
) {
    companion object {
        val EMPTY = OrderList()
    }
}