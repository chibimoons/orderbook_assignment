package com.junyoung.ha.common.bloc

import kotlinx.coroutines.flow.Flow

interface ActionMapper<STATE, ACTION> {
    suspend fun mapActionToState(state: STATE, action: ACTION): Flow<STATE>
}