package com.junyoung.ha.common.blocviewmodel

import androidx.lifecycle.ViewModel
import com.junyoung.ha.common.bloc.Bloc

open class BlocViewModel<STATE: Any, ACTION: Any>(
    private val bloc: Bloc<STATE, ACTION>
) : ViewModel() {

    val stateFlow = bloc.stateFlow

    val currentState: STATE
        get() = bloc.currentState

    init {
        bloc.start()
    }

    fun dispatch(action: ACTION) {
        bloc.dispatch(action)
    }

    override fun onCleared() {
        bloc.end()
        super.onCleared()
    }
}