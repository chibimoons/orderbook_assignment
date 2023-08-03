package com.junyoung.ha.features.orderbook.domain.model

import com.junyoung.ha.features.common.domain.Price
import java.math.BigDecimal

data class OrderInfo(
    val id: String,
    val orderType: OrderType,
    val quantity: BigDecimal,
    val price: Price,
)