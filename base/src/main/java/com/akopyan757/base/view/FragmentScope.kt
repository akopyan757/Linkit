package com.akopyan757.base.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import java.lang.Exception

fun <T> LiveData<out BaseViewModel.ResponseState<T>>.errorResponse(
    viewLifecycleOwner: LifecycleOwner,
    onAction: (Exception) -> Unit
) {
    val responseLiveData = MediatorLiveData<Exception>()
    responseLiveData.addSource(this) { response ->
        if (response is BaseViewModel.ResponseState.Error) {
            responseLiveData.value = response.exception
        }
    }
    responseLiveData.observe(viewLifecycleOwner, { result ->
        onAction.invoke(result)
    })
}

fun <T> LiveData<out BaseViewModel.ResponseState<T>>.successResponse(
    viewLifecycleOwner: LifecycleOwner,
    onAction: (T) -> Unit
) {
    val responseLiveData = MediatorLiveData<T>()
    responseLiveData.addSource(this) { response ->
        if (response is BaseViewModel.ResponseState.Success<T>) {
            responseLiveData.value = response.data
        }
    }
    responseLiveData.observe(viewLifecycleOwner, { result ->
        onAction.invoke(result)
    })
}