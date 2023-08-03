package com.junyoung.ha.features.common.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

fun BigDecimal.toQuantityFormat(): String {
    return NumberFormat.getNumberInstance().apply {
        this.maximumFractionDigits = 4
        this.minimumFractionDigits = 4
    }.format(setScale(4, RoundingMode.HALF_EVEN))
}