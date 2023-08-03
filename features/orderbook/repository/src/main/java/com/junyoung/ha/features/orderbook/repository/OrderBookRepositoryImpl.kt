package com.junyoung.ha.features.orderbook.repository

import com.junyoung.ha.features.orderbook.datasource.OrderBookDataSource
import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import com.junyoung.ha.features.orderbook.domain.repository.OrderBookRepository
import kotlinx.coroutines.flow.Flow

class OrderBookRepositoryImpl(
    private val orderBookDataSource: OrderBookDataSource
): OrderBookRepository {
    override suspend fun observeOrderBook(): Flow<OrderBook> {
        return orderBookDataSource.observeOrderBook()
    }
}