package com.junyoung.ha.features.recenttrades.datasource

import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import kotlinx.coroutines.flow.Flow

class RecentTradesDataSourceImpl: RecentTradesDataSource {
    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        TODO("Not yet implemented")
    }
}