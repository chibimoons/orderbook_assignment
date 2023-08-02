package com.junyoung.ha.features.orderbook.datasource

import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow

class OrderBookDataSourceImpl: OrderBookDataSource {
    override suspend fun observeOrderBook(): Flow<OrderBook> {
        TODO("Not yet implemented")
    }
}