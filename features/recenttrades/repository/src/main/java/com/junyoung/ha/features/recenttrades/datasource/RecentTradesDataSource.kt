package com.junyoung.ha.features.recenttrades.datasource

import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import kotlinx.coroutines.flow.Flow

interface RecentTradesDataSource {
    suspend fun observeRecentTrades(): Flow<RecentTrades>
}