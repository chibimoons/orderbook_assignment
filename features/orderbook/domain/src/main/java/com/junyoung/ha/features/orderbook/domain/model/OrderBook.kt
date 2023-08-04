package com.junyoung.ha.features.orderbook.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.lang.Integer.max
import java.math.BigDecimal
import java.math.RoundingMode

data class OrderBook(
    val buyOrderList: OrderList = OrderList.EMPTY,
    val sellOrderList: OrderList = OrderList.EMPTY
) {
    companion object {
        val EMPTY = OrderBook()
    }

    val maxOrderListSize: Int
        get() = max(buyOrderList.orderInfoList.size, sellOrderList.orderInfoList.size)
    private fun getMaxCumulativeQuantity(): BigDecimal {
        return buyOrderList.maxCumulativeQuantity.max(sellOrderList.maxCumulativeQuantity)
    }

    fun getRelativeQuantity(orderType: OrderType, price: Price): Float {
        return when (orderType) {
            OrderType.BUY -> {
                buyOrderList.getCumulativeQuantity(price).setScale(4, RoundingMode.HALF_EVEN) / getMaxCumulativeQuantity()
            }
            OrderType.SELL -> {
                sellOrderList.getCumulativeQuantity(price).setScale(4, RoundingMode.HALF_EVEN) / getMaxCumulativeQuantity()
            }
        }.toFloat()
    }
}