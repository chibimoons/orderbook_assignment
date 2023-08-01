package com.junyoung.ha.common.bloc

import com.junyoung.ha.common.bloc.ActionMapper
import com.junyoung.ha.common.bloc.ActionTransformer
import com.junyoung.ha.common.bloc.ErrorMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SimpleBlocTestSpec {
    sealed class Action {
        object A: Action()
        object B: Action()
        object C: Action()
        object D: Action()
        object E: Action()
        object F: Action()
        data class Error(val action: Action, val throwable: Throwable): Action()
    }

    data class State(
        val message: String = "",
        val throwable: Throwable? = null
    )

    class ActionTransformer: com.junyoung.ha.common.bloc.ActionTransformer<Action> {
        override suspend fun transformActions(action: Action): Flow<Action> {
            return flow { emit(action) }
        }

    }

    class ActionMapper: com.junyoung.ha.common.bloc.ActionMapper<State, Action> {
        override suspend fun mapActionToState(state: State, action: Action): Flow<State> {
            return flow {
                emit(state.copy(message = state.message + action.javaClass.simpleName))
            }
        }
    }

    class ErrorMapper: com.junyoung.ha.common.bloc.ErrorMapper<State, Action> {
        override suspend fun mapErrorToAction(
            state: State,
            action: Action,
            throwable: Throwable,
            onUnHandledError: (Throwable) -> Unit
        ): Flow<Action> {
            return flow {
                emit(Action.Error(action, throwable))
            }
        }

        override suspend fun mapErrorToState(
            state: State,
            action: Action,
            throwable: Throwable,
            onUnHandledError: (Throwable) -> Unit
        ): Flow<State> {
            return flow {
                emit(state.copy(throwable = throwable))
            }
        }

    }
}