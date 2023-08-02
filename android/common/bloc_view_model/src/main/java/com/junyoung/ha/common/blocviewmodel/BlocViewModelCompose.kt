package com.junyoung.ha.common.blocviewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun <S : Any, A : Any> LaunchInitialAction(
    initialState: S,
    initialAction: A,
    bloc: BlocViewModel<S, A>,
    curState: S
) {
    LaunchedEffect(bloc) {
        if (curState == initialState) {
            bloc.dispatch(initialAction)
        }
    }
}