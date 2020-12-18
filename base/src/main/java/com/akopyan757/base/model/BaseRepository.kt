package com.akopyan757.base.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher

abstract class BaseRepository {

    abstract val coroutineDispatcher: CoroutineDispatcher

    protected fun <T : Any> callIO(
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