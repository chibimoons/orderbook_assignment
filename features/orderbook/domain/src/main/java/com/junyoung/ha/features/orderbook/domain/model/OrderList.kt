package com.junyoung.ha.features.orderbook.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.math.BigDecimal


data class OrderList(
    val orderInfoList: List<OrderInfo> = emptyList(),
    private val priceToQuantityMap: Map<Price, BigDecimal> = emptyMap(),
    private val priceToIndexMap: Map<Price, Int> = emptyMap(),
    private val indexToCumulativeQuantityMap: Map<Int, BigDecimal> = emptyMap(),
) {
    companion object {
        val EMPTY = OrderList()
    }
    val maxCumulativeQuantity: BigDecimal
        get() {
            return if (orderInfoList.isEmpty()) {
                BigDecimal.ZERO
            } else if (indexToCumulativeQuantityMap.containsKey(orderInfoList.size - 1)) {
                indexToCumulativeQuantityMap[orderInfoList.size - 1] ?: BigDecimal.ZERO
            } else {
                BigDecimal.ZERO
            }
        }

    fun getCumulativeQuantity(price: Price): BigDecimal {
        return if (priceToIndexMap.containsKey(price)) {
            val index = priceToIndexMap[price]
            if (indexToCumulativeQuantityMap.containsKey(index)) {
                indexToCumulativeQuantityMap[index] ?: BigDecimal.ZERO
            } else {
                BigDecimal.ZERO
            }
        } else {
            BigDecimal.ZERO
        }
    }

    fun getQuantity(price: Price): BigDecimal {
        return if (priceToQuantityMap.containsKey(price)) {
            priceToQuantityMap[price] ?: BigDecimal.ZERO
        } else {
            BigDecimal.ZERO
        }
    }
}