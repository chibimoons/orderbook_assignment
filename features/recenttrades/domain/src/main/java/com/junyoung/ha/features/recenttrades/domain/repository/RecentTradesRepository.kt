package com.junyoung.ha.features.recenttrades.domain.repository

import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import kotlinx.coroutines.flow.Flow

interface RecentTradesRepository {
    suspend fun observeRecentTrades(): Flow<RecentTrades>
}