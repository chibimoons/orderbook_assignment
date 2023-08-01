package com.junyoung.ha.common.bloc

data class Transition<out STATE: Any, out ACTION: Any>(
    val currentState: STATE,
    val action: ACTION,
    val nextState: STATE
)
