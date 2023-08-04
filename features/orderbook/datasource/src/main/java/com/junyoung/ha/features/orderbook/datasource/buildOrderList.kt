package com.junyoung.ha.features.orderbook.datasource

import com.junyoung.ha.features.orderbook.domain.model.OrderInfo
import com.junyoung.ha.features.orderbook.domain.model.OrderList
import java.math.BigDecimal
import java.math.RoundingMode

fun buildOrderList(
    orderInfoList: List<OrderInfo>
): OrderList {
    var cumulativeQuantity = BigDecimal.ZERO
    val calculatedOrderInfoList = orderInfoList.map {
        cumulativeQuantity += it.quantity
        it.copy(cumulativeQuantity = cumulativeQuantity.setScale(4, RoundingMode.HALF_EVEN))
    }
    return OrderList(
        orderInfoList = calculatedOrderInfoList,
        maxCumulativeQuantity = cumulativeQuantity
    )
}