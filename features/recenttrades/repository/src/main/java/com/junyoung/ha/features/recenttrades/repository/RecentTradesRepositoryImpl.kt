package com.junyoung.ha.features.recenttrades.repository

import com.junyoung.ha.features.recenttrades.datasource.RecentTradesDataSource
import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.repository.RecentTradesRepository
import kotlinx.coroutines.flow.Flow

class RecentTradesRepositoryImpl(
    private val recentTradesDataSource: RecentTradesDataSource
): RecentTradesRepository {
    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        return recentTradesDataSource.observeRecentTrades()
    }
}