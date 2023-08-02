package com.junyoung.ha.assignment.ui.home

import com.junyoung.ha.common.bloc.Bloc
import com.junyoung.ha.common.blocviewmodel.BlocViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeUiViewModel @Inject constructor(
    bloc: Bloc<HomeUi.State, HomeUi.Action>
): BlocViewModel<HomeUi.State, HomeUi.Action>(bloc)