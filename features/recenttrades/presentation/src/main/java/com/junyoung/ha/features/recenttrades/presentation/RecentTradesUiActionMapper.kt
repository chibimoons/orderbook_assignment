package com.junyoung.ha.features.recenttrades.presentation

import com.junyoung.ha.common.bloc.ActionMapper
import com.junyoung.ha.features.recenttrades.domain.usecase.RecentTradesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecentTradesUiActionMapper(
    private val recentTradesUseCase: RecentTradesUseCase
): ActionMapper<RecentTradesUi.State, RecentTradesUi.Action> {
    override suspend fun mapActionToState(
        state: RecentTradesUi.State,
        action: RecentTradesUi.Action
    ): Flow<RecentTradesUi.State> {
        return when (action) {
            RecentTradesUi.Action.Start -> start(state)
        }
    }

    suspend fun start(
        state: RecentTradesUi.State
    ): Flow<RecentTradesUi.State> {
        return flow {
            emit(state.copy(recentTradesFlow = recentTradesUseCase.observeRecentTrades()))
        }
    }
}