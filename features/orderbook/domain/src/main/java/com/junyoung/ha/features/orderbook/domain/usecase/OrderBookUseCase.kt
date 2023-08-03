package com.junyoung.ha.features.orderbook.domain.usecase

import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import com.junyoung.ha.features.orderbook.domain.repository.OrderBookRepository
import kotlinx.coroutines.flow.Flow

interface OrderBookUseCase {
    suspend fun observeOrderBook(): Flow<OrderBook>
}

class OrderBookUseCaseImpl(
    private val orderBookRepository: OrderBookRepository
): OrderBookUseCase {
    override suspend fun observeOrderBook(): Flow<OrderBook> {
        return orderBookRepository.observeOrderBook()
    }

}