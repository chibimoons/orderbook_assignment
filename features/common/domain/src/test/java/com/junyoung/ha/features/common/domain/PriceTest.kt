package com.junyoung.ha.features.common.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
class PriceTest {
    @Test
    fun `비교 연산자 테스트`() {
        Assert.assertTrue(Price(0).isZero())
        Assert.assertTrue(Price(0L).isZero())
        Assert.assertTrue(Price(0.00).isZero())

        Assert.assertTrue(Price(100.1) < Price(150.2))
        Assert.assertTrue(Price(100.1) <= Price(150.2))
        Assert.assertTrue(Price(150.2) <= Price(150.2))

        Assert.assertTrue(Price(200.1) > Price(100.1))
        Assert.assertTrue(Price(200.1) >= Price(200.1))
        Assert.assertTrue(Price(200.1) >= Price(200.0))

        Assert.assertTrue(Price(100.1) == Price(100.1))
        Assert.assertTrue(Price(100.0) != Price(100))
    }

    data class PricePrintTestSpec(
        val actual: String,
        val expect: String
    ) {
    }

    fun PricePrintTestSpec.run() {
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `가격 단위 소수점 1자리 출력 테스트`() {
        PricePrintTestSpec(
            actual = Price(100).toFormattedString(),
            expect = "100.0"
        ).run()

        PricePrintTestSpec(
            actual = Price(51682.0).toFormattedString(),
            expect = "51,682.0"
        ).run()

        PricePrintTestSpec(
            actual = Price(51682.01).toFormattedString(),
            expect = "51,682.0"
        ).run()

        PricePrintTestSpec(
            actual = Price(51682.05).toFormattedString(),
            expect = "51,682.0"
        ).run()

        PricePrintTestSpec(
            actual = Price(51682.06).toFormattedString(),
            expect = "51,682.1"
        ).run()
    }
}