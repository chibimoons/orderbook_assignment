package com.junyoung.ha.common.bloc

import com.junyoung.ha.common.bloc.ActionMapper
import com.junyoung.ha.common.bloc.ActionTransformer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CancelBlocTestSpec {
    sealed class Action {
        object LongWorkForCancelAction: Action()
        object NewWorkAction: Action()
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
            return when (action) {
                Action.LongWorkForCancelAction -> flow {
                    delay(Long.MAX_VALUE)
                    emit(state.copy(message = action.javaClass.simpleName))
                }
                Action.NewWorkAction -> flow { emit(state.copy(message = action.javaClass.simpleName)) }
            }
        }
    }
}