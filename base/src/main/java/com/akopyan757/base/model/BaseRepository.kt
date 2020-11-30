package com.akopyan757.base.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher

abstract class BaseRepository {

    protected fun <T : Any> call(
        dispatcher: CoroutineDispatcher,
        action: suspend () -> T?
    ) = liveData(dispatcher) {
        emit(ApiResponse.Loading)

        try {
            val data = action.invoke()
            if (data != null) {
                emit(ApiResponse.Success(data))
            } else {
                emit(ApiResponse.Error(NullPointerException()))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e))
        }
    }

    protected fun <T> callLive(
        dispatcher: CoroutineDispatcher,
        action: () -> LiveData<T>
    ) = liveData(dispatcher) {
        emitSource(action.invoke())
    }
}