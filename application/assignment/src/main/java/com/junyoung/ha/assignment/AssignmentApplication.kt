package com.junyoung.ha.assignment

import android.app.Application
import android.util.Log
import com.junyoung.ha.common.bloc.BlocDelegate
import com.junyoung.ha.common.bloc.BlocSupervisor
import com.junyoung.ha.common.bloc.Transition
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AssignmentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initBlocSupervisor()
    }

    private fun initBlocSupervisor() {
        BlocSupervisor.delegate = object : BlocDelegate<Any, Any> {
            override suspend fun onAction(action: Any) {
                Log.d("Bloc", "onAction:${action}")
            }

            override suspend fun onTransition(transition: Transition<Any, Any>) {
                Log.d("Bloc", "onTransition:${transition}")
            }

            override suspend fun onError(throwable: Throwable) {
                Log.d("Bloc", "throwable:${throwable}")
            }

        }
    }
}