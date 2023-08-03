package com.junyoung.ha.features.orderbook.domain.model

import com.junyoung.ha.features.common.domain.Price
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
    private fun getMaxCumulativeQuantity(): BigDecimal {
        return buyOrderList.maxCumulativeQuantity.max(sellOrderList.maxCumulativeQuantity)
    }

    fun getRelativeQuantity(orderType: OrderType, price: Price): Float {
        return when (orderType) {
            OrderType.BUY -> {
                buyOrderList.getCumulativeQuantity(price) / getMaxCumulativeQuantity()
            }
            OrderType.SELL -> {
                sellOrderList.getCumulativeQuantity(price) / getMaxCumulativeQuantity()
            }
        }.toFloat()
    }
}