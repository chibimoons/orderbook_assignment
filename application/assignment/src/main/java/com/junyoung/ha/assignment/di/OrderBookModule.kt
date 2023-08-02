package com.junyoung.ha.assignment.di

import com.junyoung.ha.common.bloc.Bloc
import com.junyoung.ha.features.orderbook.datasource.OrderBookDataSource
import com.junyoung.ha.features.orderbook.datasource.OrderBookDataSourceImpl
import com.junyoung.ha.features.orderbook.datasource.OrderBookDataSourceMock
import com.junyoung.ha.features.orderbook.domain.repository.OrderBookRepository
import com.junyoung.ha.features.orderbook.domain.usecase.OrderBookUseCase
import com.junyoung.ha.features.orderbook.domain.usecase.OrderBookUseCaseImpl
import com.junyoung.ha.features.orderbook.presentation.OrderBookUi
import com.junyoung.ha.features.orderbook.presentation.OrderBookUiActionMapper
import com.junyoung.ha.features.orderbook.repository.OrderBookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

class OrderBookModule {
    @Module
    @InstallIn(ViewModelComponent::class)
    class DomainModule {
        @Provides
        fun provideOrderBookUseCase(orderBookRepository: OrderBookRepository): OrderBookUseCase {
            return OrderBookUseCaseImpl(orderBookRepository)
        }
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    class PresentationModule {
        @Provides
        fun provideOrderBookUiActionMapper(orderBookUseCase: OrderBookUseCase): OrderBookUiActionMapper {
            return OrderBookUiActionMapper(orderBookUseCase)
        }
        @Provides
        fun provideOrderBookUiBloc(actionMapper: OrderBookUiActionMapper): Bloc<OrderBookUi.State, OrderBookUi.Action> {
            return Bloc(
                initialState = OrderBookUi.State.INITIAL_STATE,
                actionMapper = actionMapper
            )
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class RepositoryModule {
        @Provides
        fun provideOrderBookRepository(orderBookDataSource: OrderBookDataSource): OrderBookRepository {
            return OrderBookRepositoryImpl(orderBookDataSource)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class DataSourceModule {
        companion object {
            const val USING_MOCK = true
        }

        @Provides
        fun provideOrderBookDataSource(): OrderBookDataSource {
            return if (USING_MOCK) {
                OrderBookDataSourceMock()
            } else {
                OrderBookDataSourceImpl()
            }
        }
    }
}