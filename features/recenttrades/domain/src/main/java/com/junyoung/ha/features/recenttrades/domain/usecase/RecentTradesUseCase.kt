package com.junyoung.ha.features.recenttrades.domain.usecase

import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.repository.RecentTradesRepository
import kotlinx.coroutines.flow.Flow

interface RecentTradesUseCase {
    suspend fun observeRecentTrades(): Flow<RecentTrades>
}

class RecentTradesUseCaseImpl(
    private val recentTradesRepository: RecentTradesRepository
): RecentTradesUseCase {
    override suspend fun observeRecentTrades(): Flow<RecentTrades> {
        return recentTradesRepository.observeRecentTrades()
    }

}