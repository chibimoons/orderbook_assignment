package com.junyoung.ha.common.bloc

interface BlocDelegate<STATE: Any, ACTION: Any> {
    suspend fun onAction(action: ACTION)
    suspend fun onTransition(transition: Transition<STATE, ACTION>)
    suspend fun onError(throwable: Throwable)
}

class DefaultDelegate<STATE: Any, ACTION: Any> : BlocDelegate<STATE, ACTION> {
    override suspend fun onAction(action: ACTION) {
        // Nothing to do
    }

    override suspend fun onTransition(transition: Transition<STATE, ACTION>) {
        // Nothing to do
    }

    override suspend fun onError(throwable: Throwable) {
        // Nothing to do
    }
}