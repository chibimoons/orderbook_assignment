package com.junyoung.ha.common.bloc

object BlocSupervisor {
    var delegate: BlocDelegate<Any, Any>? = null
}