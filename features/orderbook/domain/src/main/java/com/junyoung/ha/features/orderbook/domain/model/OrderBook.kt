package com.junyoung.ha.features.orderbook.domain.model

import java.lang.Integer.max
import java.math.BigDecimal

data class OrderBook(
    val buyOrderList: OrderList = OrderList.EMPTY,
    val sellOrderList: OrderList = OrderList.EMPTY
) {
    companion object {
        val EMPTY = OrderBook()
    }

    val maxOrderListSize: Int
        get() = max(buyOrderList.orderInfoList.size, sellOrderList.orderInfoList.size)

    fun getRelativeQuantity(orderInfo: OrderInfo): Float {
        return getMaxCumulativeQuantity().let {
            if (it == BigDecimal.ZERO) {
                0f
            } else {
                (orderInfo.cumulativeQuantity / it).toFloat()
            }
        }
    }

    private fun getMaxCumulativeQuantity(): BigDecimal {
        return buyOrderList.maxCumulativeQuantity.max(sellOrderList.maxCumulativeQuantity)
    }
}