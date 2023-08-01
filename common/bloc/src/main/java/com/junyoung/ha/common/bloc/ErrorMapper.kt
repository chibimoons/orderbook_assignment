package com.junyoung.ha.common.bloc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface ErrorMapper<STATE, ACTION> {
    suspend fun mapErrorToAction(state: STATE, action: ACTION, throwable: Throwable, onUnHandledError: (Throwable) -> Unit): Flow<ACTION>
    suspend fun mapErrorToState(state: STATE, action: ACTION, throwable: Throwable, onUnHandledError: (Throwable) -> Unit): Flow<STATE>
}

class DefaultErrorMapper<STATE, ACTION>: ErrorMapper<STATE, ACTION> {
    override suspend fun mapErrorToAction(
        state: STATE,
        action: ACTION,
        throwable: Throwable,
        onUnHandledError: (Throwable) -> Unit
    ): Flow<ACTION> {
        onUnHandledError(throwable)
        return emptyFlow()
    }

    override suspend fun mapErrorToState(
        state: STATE,
        action: ACTION,
        throwable: Throwable,
        onUnHandledError: (Throwable) -> Unit
    ): Flow<STATE> {
        onUnHandledError(throwable)
        return emptyFlow()
    }

}