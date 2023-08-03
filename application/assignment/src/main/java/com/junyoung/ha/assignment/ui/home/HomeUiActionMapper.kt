package com.junyoung.ha.assignment.ui.home

import com.junyoung.ha.common.bloc.ActionMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeUiActionMapper: ActionMapper<HomeUi.State, HomeUi.Action> {
    override suspend fun mapActionToState(
        state: HomeUi.State,
        action: HomeUi.Action
    ): Flow<HomeUi.State> {
        return when (action) {
            is HomeUi.Action.OnSelectedTab -> onSelectedTab(state, action)
        }
    }

    private suspend fun onSelectedTab(
        state: HomeUi.State,
        action: HomeUi.Action.OnSelectedTab
    ): Flow<HomeUi.State> {
        return flow {
            emit(state.copy(currentTab = action.tab))
        }
    }
}