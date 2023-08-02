package com.junyoung.ha.assignment.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.junyoung.ha.assignment.R
import com.junyoung.ha.assignment.ui.composables.TBDComposable
import com.junyoung.ha.features.orderbook.ui.OrderBookRenderer
import com.junyoung.hafeatures.recenttrades.ui.RecentTradesRenderer

@Composable
fun HomeUiRenderer(viewModel: HomeUiViewModel) {
    val state by viewModel.stateFlow.collectAsState()
    Text(text = state.currentTab.name)
    Content(state, viewModel::dispatch)

}

@Composable
private fun Content(state: HomeUi.State, onAction: (HomeUi.Action) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TabRow(selectedTabIndex = state.currentTabIndex) {
            state.tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { TabLabel(tab) },
                    selected = state.currentTab == tab,
                    onClick = { onAction(HomeUi.Action.OnSelectedTab(tab)) }
                )
            }
        }
        when (state.tabs[state.currentTabIndex]) {
            HomeUi.Tab.CHART -> TBDComposable(modifier = Modifier.weight(1f))
            HomeUi.Tab.ORDER_BOOK -> OrderBookRenderer()
            HomeUi.Tab.RECENT_TRADE -> RecentTradesRenderer()
        }
    }
}

@Composable
private fun TabLabel(tab: HomeUi.Tab) {
    Text(text = getTabLabel(tab = tab))
}

@Composable
private fun getTabLabel(tab: HomeUi.Tab): String {
    return when (tab) {
        HomeUi.Tab.CHART -> stringResource(id = R.string.tab_label_chart)
        HomeUi.Tab.ORDER_BOOK -> stringResource(id = R.string.tab_label_order_book)
        HomeUi.Tab.RECENT_TRADE -> stringResource(id = R.string.tab_label_recent_trades)
    }
}