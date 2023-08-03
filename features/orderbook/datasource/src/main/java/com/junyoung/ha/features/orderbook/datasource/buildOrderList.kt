package com.junyoung.ha.features.orderbook.datasource

import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.orderbook.domain.model.OrderInfo
import com.junyoung.ha.features.orderbook.domain.model.OrderList
import java.math.BigDecimal

fun buildOrderList(orderInfoList: List<OrderInfo>): OrderList {
    val priceToQuantityMap = mutableMapOf<Price, BigDecimal>()
    val priceToIndexMap = mutableMapOf<Price, Int>()
    val indexToCumulativeQuantityMap = mutableMapOf<Int, BigDecimal>()
    orderInfoList.forEachIndexed { index, orderInfo ->
        priceToQuantityMap[orderInfo.price] = orderInfo.quantity
        priceToIndexMap[orderInfo.price] = index
        indexToCumulativeQuantityMap[index] = if (index == 0) {
            orderInfo.quantity
        } else {
            (indexToCumulativeQuantityMap[index - 1] ?: BigDecimal.ZERO) + orderInfo.quantity
        }
    }
    return OrderList(
        orderInfoList = orderInfoList,
        priceToQuantityMap = priceToQuantityMap,
        priceToIndexMap = priceToIndexMap,
        indexToCumulativeQuantityMap = indexToCumulativeQuantityMap
    )
}