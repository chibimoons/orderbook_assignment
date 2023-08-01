package com.junyoung.ha.common.bloc

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class Bloc<STATE: Any, ACTION: Any>(
    private val initialState: STATE,
    private val actionTransformer: ActionTransformer<ACTION> = DefaultTransformer(),
    private val actionMapper: ActionMapper<STATE, ACTION>,
    private val errorMapper: ErrorMapper<STATE, ACTION> = DefaultErrorMapper(),
    private val delegate: BlocDelegate<STATE, ACTION> = DefaultDelegate(),
    private val blocScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {
    private val stateMutableFlow by lazy { MutableStateFlow(initialState) }

    val stateFlow = stateMutableFlow.asStateFlow()
    val currentState: STATE
        get() = stateFlow.value

    private val actionFlow = Channel<ACTION>()
    private var job: Job? = null

    fun start() {
        job = actionFlow.receiveAsFlow()
            .flatMapMerge { action -> transformActions(action) }
            .flatMapConcat { action -> mapActionToState(action) }
            .filter { it.currentState != it.nextState }
            .onEach { transition -> doOnTransition(transition) }
            .catch {
                doOnError(it)
                start()
            }
            .launchIn(blocScope)
    }

    private suspend fun transformActions(action: ACTION): Flow<ACTION> {
        return actionTransformer.transformActions(action)
            .catch {
                emitAll(errorMapper.mapErrorToAction(currentState, action, it) {
                    blocScope.launch {
                        doOnError(it)
                    }
                })
            }
    }

    private suspend fun mapActionToState(action: ACTION): Flow<Transition<STATE, ACTION>> {
        return actionMapper.mapActionToState(currentState, action)
            .catch {
                emitAll(errorMapper.mapErrorToState(currentState, action, it) {
                    blocScope.launch {
                        doOnError(it)
                    }
                })
            }
            .map { Transition(currentState, action, it) }
    }

    fun dispatch(action: ACTION) {
        blocScope.launch {
            doOnAction(action)
            actionFlow.send(action)
        }
    }

    fun cancelAllAction() {
        job?.cancel()
        start()
    }

    private suspend fun doOnAction(action: ACTION) {
        BlocSupervisor.delegate?.onAction(action)
        delegate.onAction(action)
    }

    private suspend fun doOnTransition(transition: Transition<STATE, ACTION>) {
        BlocSupervisor.delegate?.onTransition(transition)
        delegate.onTransition(transition)
        stateMutableFlow.emit(transition.nextState)
    }

    private suspend fun doOnError(throwable: Throwable) {
        BlocSupervisor.delegate?.onError(throwable)
        delegate.onError(throwable)
    }

    fun end() {
        blocScope.cancel()
        actionFlow.close()
    }
}