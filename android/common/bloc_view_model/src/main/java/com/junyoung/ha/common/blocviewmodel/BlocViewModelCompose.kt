package com.junyoung.ha.common.blocviewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun <S : Any, A : Any> LaunchInitialAction(
    initialState: S,
    initialAction: A,
    viewModel: BlocViewModel<S, A>,
    curState: S
) {
    LaunchedEffect(viewModel) {
        if (curState == initialState) {
            viewModel.dispatch(initialAction)
        }
    }
}