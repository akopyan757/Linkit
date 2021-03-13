package com.akopyan757.base.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseRepository {

    abstract val coroutineDispatcher: CoroutineDispatcher

    protected fun launchIO(action: suspend () -> Unit) {
        CoroutineScope(coroutineDispatcher).launch { action.invoke() }
    }

    protected fun wrapActionIO(
        action: suspend () -> Unit
    ) = liveData(coroutineDispatcher) {
        emit(ApiResponse.Loading)
        try {
            action.invoke()
            emit(ApiResponse.Success(Unit))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e))
        }
    }

    protected fun <T : Any> wrapActionIOWithResult(
        action: suspend () -> T
    ) = liveData(coroutineDispatcher) {
        emit(ApiResponse.Loading)
        try {
            val data = action.invoke()
            emit(ApiResponse.Success(data))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e))
        }
    }

    protected fun <T> LiveData<T>.asLiveIO() = liveData(coroutineDispatcher) {
        emitSource(this@asLiveIO)
    }
}