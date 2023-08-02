package com.junyoung.ha.features.orderbook.domain.repository

import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow

interface OrderBookRepository {
    suspend fun observeOrderBook(): Flow<OrderBook>
}