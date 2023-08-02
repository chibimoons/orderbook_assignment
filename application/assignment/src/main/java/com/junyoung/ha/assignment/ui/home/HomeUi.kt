package com.junyoung.ha.assignment.ui.home

object HomeUi {
    enum class Tab {
        CHART,
        ORDER_BOOK,
        RECENT_TRADE
    }
    sealed class Action {
        data class OnSelectedTab(val tab: Tab): Action()
    }

    data class State(
        val currentTab: Tab = Tab.ORDER_BOOK,
        val tabs: List<Tab> = listOf(Tab.CHART, Tab.ORDER_BOOK, Tab.RECENT_TRADE)
    ) {
        companion object {
            val INITIAL_STATE = State()
        }

        val currentTabIndex: Int
            get() = tabs.indexOf(currentTab)
    }
}