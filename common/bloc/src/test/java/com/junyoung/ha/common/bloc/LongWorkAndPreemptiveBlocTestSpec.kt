package com.junyoung.ha.common.bloc

import com.junyoung.ha.common.bloc.ActionMapper
import com.junyoung.ha.common.bloc.ActionTransformer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object LongWorkAndPreemptiveBlocTestSpec {
    sealed class Action {
        object LongWorkAction: Action()
        object PreemptiveAction: Action()
    }

    data class State(
        val message: String = "",
        val throwable: Throwable? = null
    )

    interface TestUseCase {
        suspend fun workPreemptive()
    }

    class ActionTransformer(private val testUseCase: TestUseCase):
        com.junyoung.ha.common.bloc.ActionTransformer<Action> {
        override suspend fun transformActions(action: Action): Flow<Action> {
            return when (action) {
                Action.LongWorkAction -> flow {
                    emit(action)
                }
                Action.PreemptiveAction -> flow {
                    testUseCase.workPreemptive()
                }
            }
        }

    }

    class ActionMapper: com.junyoung.ha.common.bloc.ActionMapper<State, Action> {
        override suspend fun mapActionToState(state: State, action: Action): Flow<State> {
            return when (action) {
                Action.LongWorkAction -> flow {
                    delay(Long.MAX_VALUE)
                    emit(state.copy(message = action.javaClass.simpleName))
                }
                Action.PreemptiveAction -> flow { emit(state.copy(message = action.javaClass.simpleName)) }
            }
        }
    }
}