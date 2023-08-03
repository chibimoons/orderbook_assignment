package com.junyoung.ha.features.orderbook.presentation

import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object OrderBookUi {
    sealed class Action {
        object Start: Action()
    }

    data class State(
        val orderBookFlow: Flow<OrderBook> = emptyFlow()
    ) {
        companion object {
            val INITIAL_STATE = State()
        }
    }
}