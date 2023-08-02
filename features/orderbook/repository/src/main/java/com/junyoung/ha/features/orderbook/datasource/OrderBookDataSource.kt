package com.junyoung.ha.features.orderbook.datasource

import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow

interface OrderBookDataSource {
    suspend fun observeOrderBook(): Flow<OrderBook>
}