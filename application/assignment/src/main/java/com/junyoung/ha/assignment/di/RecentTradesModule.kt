package com.junyoung.ha.assignment.di

import com.junyoung.ha.common.bloc.Bloc
import com.junyoung.ha.features.recenttrades.datasource.RecentTradesDataSource
import com.junyoung.ha.features.recenttrades.datasource.RecentTradesDataSourceImpl
import com.junyoung.ha.features.recenttrades.datasource.RecentTradesDataSourceMock
import com.junyoung.ha.features.recenttrades.domain.repository.RecentTradesRepository
import com.junyoung.ha.features.recenttrades.domain.usecase.RecentTradesUseCase
import com.junyoung.ha.features.recenttrades.domain.usecase.RecentTradesUseCaseImpl
import com.junyoung.ha.features.recenttrades.presentation.RecentTradesUi
import com.junyoung.ha.features.recenttrades.presentation.RecentTradesUiActionMapper
import com.junyoung.ha.features.recenttrades.repository.RecentTradesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

class RecentTradesModule {
    @Module
    @InstallIn(ViewModelComponent::class)
    class DomainModule {
        @Provides
        fun provideRecentTradesUseCase(recentTradesRepository: RecentTradesRepository): RecentTradesUseCase {
            return RecentTradesUseCaseImpl(recentTradesRepository)
        }
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    class PresentationModule {
        @Provides
        fun provideRecentTradesUiActionMapper(recentTradesUseCase: RecentTradesUseCase): RecentTradesUiActionMapper {
            return RecentTradesUiActionMapper(recentTradesUseCase)
        }
        @Provides
        fun provideRecentTradesUiBloc(actionMapper: RecentTradesUiActionMapper): Bloc<RecentTradesUi.State, RecentTradesUi.Action> {
            return Bloc(
                initialState = RecentTradesUi.State.INITIAL_STATE,
                actionMapper = actionMapper
            )
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class RepositoryModule {
        @Provides
        fun provideRecentTradesRepository(recentTradesDataSource: RecentTradesDataSource): RecentTradesRepository {
            return RecentTradesRepositoryImpl(recentTradesDataSource)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class DataSourceModule {
        companion object {
            const val USING_MOCK = true
        }

        @Provides
        fun provideRecentTradesDataSource(): RecentTradesDataSource {
            return if (USING_MOCK) {
                RecentTradesDataSourceMock()
            } else {
                RecentTradesDataSourceImpl()
            }
        }
    }
}