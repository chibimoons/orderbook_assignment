package com.junyoung.ha.features.recenttrades.ui

import com.junyoung.ha.common.bloc.Bloc
import com.junyoung.ha.common.blocviewmodel.BlocViewModel
import com.junyoung.ha.features.recenttrades.presentation.RecentTradesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentTradesViewModel @Inject constructor(
    bloc: Bloc<RecentTradesUi.State, RecentTradesUi.Action>
): BlocViewModel<RecentTradesUi.State, RecentTradesUi.Action>(bloc)