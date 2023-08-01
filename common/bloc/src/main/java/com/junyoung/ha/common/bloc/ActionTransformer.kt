package com.junyoung.ha.common.bloc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ActionTransformer<ACTION> {
    suspend fun transformActions(action: ACTION): Flow<ACTION>
}

class DefaultTransformer<ACTION> : ActionTransformer<ACTION> {
    override suspend fun transformActions(action: ACTION): Flow<ACTION> {
        return flowOf(action)
    }
}