package com.junyoung.ha.features.common.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

data class Price(
    val amount: BigDecimal
): Comparable<Price> {
    companion object {
        val ZERO = Price(BigDecimal.ZERO)
    }

    constructor(amount: String): this(
        BigDecimal(amount)
    )

    constructor(amount: Long): this(
        BigDecimal(amount)
    )

    constructor(amount: Double): this(
        BigDecimal(amount.toString())
    )

    operator fun plus(other: Price) : Price {
        return Price(this.amount.add(other.amount))
    }

    operator fun plus(other: Long) : Price {
        return Price(
            this.amount.plus(BigDecimal(other))
        )
    }

    operator fun minus(other: Price) : Price {
        return Price(this.amount.minus(other.amount))
    }

    operator fun minus(other: Long) : Price {
        return Price(
            this.amount.minus(BigDecimal(other))
        )
    }

    operator fun div(other: Price) : Price {
        return Price(this.amount.div(other.amount))
    }

    operator fun div(other: Long) : Price {
        return Price(
            this.amount.div(BigDecimal(other))
        )
    }

    operator fun rem(other: Price) : Price {
        return Price(this.amount.rem(other.amount))
    }

    operator fun rem(other: Long) : Price {
        return Price(
            this.amount.rem(BigDecimal(other))
        )
    }

    operator fun times(other: Price) : Price {
        return Price(this.amount.times(other.amount))
    }

    operator fun times(other: Long) : Price {
        return Price(
            this.amount.times(BigDecimal(other))
        )
    }

    override fun compareTo(other: Price): Int {
        return compareValuesBy(this, other, Price::amount)
    }

    fun isZero() : Boolean {
        return amount.compareTo(BigDecimal.ZERO) == 0
    }
}

fun Price.toFormattedString(): String {
    return NumberFormat.getNumberInstance().apply {
        this.maximumFractionDigits = 1
        this.minimumFractionDigits = 1
    }.format(amount.setScale(1, RoundingMode.HALF_EVEN))
}