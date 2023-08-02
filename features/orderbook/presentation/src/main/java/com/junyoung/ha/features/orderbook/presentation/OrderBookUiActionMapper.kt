package com.junyoung.ha.features.orderbook.presentation

import com.junyoung.ha.common.bloc.ActionMapper
import com.junyoung.ha.features.orderbook.domain.usecase.OrderBookUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderBookUiActionMapper(
    private val orderBookUseCase: OrderBookUseCase
): ActionMapper<OrderBookUi.State, OrderBookUi.Action> {
    override suspend fun mapActionToState(
        state: OrderBookUi.State,
        action: OrderBookUi.Action
    ): Flow<OrderBookUi.State> {
        return when (action) {
            OrderBookUi.Action.Start -> start(state)
        }
    }

    suspend fun start(
        state: OrderBookUi.State,
    ): Flow<OrderBookUi.State> {
        return flow {
            emit(state.copy(orderBookFlow = orderBookUseCase.observeOrderBook()))
        }
    }
}