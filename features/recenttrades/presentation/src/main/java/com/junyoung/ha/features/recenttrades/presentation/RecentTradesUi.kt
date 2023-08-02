package com.junyoung.ha.features.recenttrades.presentation

import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object RecentTradesUi {
    sealed class Action {
        object Start: Action()
    }

    data class State(
        val recentTradesFlow: Flow<RecentTrades> = emptyFlow()
    ) {
        companion object {
            val INITIAL_STATE = State()
        }
    }
}