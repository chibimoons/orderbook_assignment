package com.junyoung.ha.features.orderbook.ui

import com.junyoung.ha.common.bloc.Bloc
import com.junyoung.ha.common.blocviewmodel.BlocViewModel
import com.junyoung.ha.features.orderbook.presentation.OrderBookUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderBookViewModel @Inject constructor(
    bloc: Bloc<OrderBookUi.State, OrderBookUi.Action>
): BlocViewModel<OrderBookUi.State, OrderBookUi.Action>(bloc)