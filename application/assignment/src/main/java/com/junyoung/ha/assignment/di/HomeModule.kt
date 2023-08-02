package com.junyoung.ha.assignment.di

import com.junyoung.ha.assignment.ui.home.HomeUi
import com.junyoung.ha.assignment.ui.home.HomeUiActionMapper
import com.junyoung.ha.common.bloc.Bloc
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HomeModule {
    @Provides
    fun provideHomeUiActionMapper(): HomeUiActionMapper {
        return HomeUiActionMapper()
    }
    @Provides
    fun provideHomeUiBloc(actionMapper: HomeUiActionMapper): Bloc<HomeUi.State, HomeUi.Action> {
        return Bloc(
            initialState = HomeUi.State.INITIAL_STATE,
            actionMapper = actionMapper
        )
    }
}


