package com.junyoung.ha.features.orderbook.datasource

import android.util.Log
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import com.junyoung.ha.features.orderbook.domain.model.OrderInfo
import com.junyoung.ha.features.orderbook.domain.model.OrderList
import com.junyoung.ha.features.orderbook.domain.model.OrderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class OrderBookDataSourceMock: OrderBookDataSource {

    private var orderBook: OrderBook = OrderBook.EMPTY
    private val orderBookMutableFlow by lazy { MutableStateFlow(OrderBook.EMPTY) }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Log.d("OrderBookDataSourceMock", "keep going")
                delay((10..1000).random().toLong())
                if (orderBook == OrderBook.EMPTY) {
                    orderBook = OrderBook(
                        buyOrderList = createOrderList(
                            listOf(
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.1709),
                                    price = Price(51816.3)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.3232),
                                    price = Price(51816.0)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.7068),
                                    price = Price(51815.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.1424),
                                    price = Price(51807.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.0651),
                                    price = Price(51801.3)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.3775),
                                    price = Price(51790.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.2761),
                                    price = Price(51787.5)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.3232),
                                    price = Price(51787.2)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.6709),
                                    price = Price(51787.1)
                                ),
                                OrderInfo(
                                    orderType = OrderType.BUY,
                                    quantity = BigDecimal(0.0038),
                                    price = Price(51779.7)
                                ),
                            )
                        ),
                        sellOrderList = createOrderList(
                            listOf(
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(3.3893),
                                    price = Price(51816.4)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(1.3259),
                                    price = Price(51816.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0463),
                                    price = Price(51818.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0281),
                                    price = Price(51824.1)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.2482),
                                    price = Price(51824.9)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0001),
                                    price = Price(51829.6)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0441),
                                    price = Price(51853.1)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0018),
                                    price = Price(51861.5)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0106),
                                    price = Price(51864.8)
                                ),
                                OrderInfo(
                                    orderType = OrderType.SELL,
                                    quantity = BigDecimal(0.0045),
                                    price = Price(51870.8)
                                ),
                            )
                        )
                    )
                } else {
                    orderBook = orderBook.copy()
                }
                orderBookMutableFlow.emit(orderBook)
            }
        }
    }

    private fun createOrderList(orderInfoList: List<OrderInfo>): OrderList {
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

    override suspend fun observeOrderBook(): Flow<OrderBook> {
        return orderBookMutableFlow.asStateFlow()
    }
}