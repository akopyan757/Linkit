package com.akopyan757.base.viewmodel

import android.util.Log
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.base.viewmodel.list.ListLiveData
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

abstract class BaseViewModel: ViewModel(), BaseStateObservable {

    private val mAction = MutableLiveData<Int>()
    private val mCallbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    sealed class ResponseState<out T> {
        object Empty: ResponseState<Nothing>()
        object Loading: ResponseState<Nothing>()
        object SuccessEmpty: ResponseState<Nothing>()
        data class Success<out T>(val data: T): ResponseState<T>()
        data class Error(val exception: Exception): ResponseState<Nothing>()
    }

    infix fun emitAction(code: Int) {
        mAction.value = code
    }

    fun getLiveAction(): LiveData<Int> = mAction

    override fun getCallback(): PropertyChangeRegistry {
        return mCallbacks
    }

    fun <T, R : DiffItemObservable> bindLiveList(
        request: LiveData<List<T>>,
        listLiveData: ListLiveData<R>,
        onMap: (List<T>) -> List<R>,
        onStart: (() -> Unit)? = null,
        onFinished: ((List<R>) -> Unit)? = null,
        onError: ((exception: Exception) -> Unit)? = null
    ) {
        listLiveData.addSource(request) { data ->
            viewModelScope.launch {
                try {
                    onStart?.invoke()
                    val observables = onMap.invoke(data)
                    listLiveData.change(observables) {
                        onFinished?.invoke(observables)
                    }
                } catch (e: Exception) {
                    onError?.invoke(e)
                }
            }
        }
    }

    fun <T> emptyLiveRequest(): LiveData<ResponseState<T>> = liveData {
        emit(ResponseState.Empty)
    }

    fun <T, V> requestConvert(
        request: LiveData<ApiResponse<T>>,
        onLoading: (() -> Unit)? = null,
        onSuccess: ((data: T) -> V)? = null,
        onError: ((exception: Exception) -> Unit)? = null
    ): LiveData<ResponseState<V>> = Transformations.map(request) { response ->
        return@map when (response) {
            is ApiResponse.Loading -> {
                onLoading?.invoke()
                ResponseState.Loading
            }

            is ApiResponse.Success -> {
                val result = onSuccess?.invoke(response.data)
                if (result != null) {
                    ResponseState.Success(result)
                } else {
                    ResponseState.SuccessEmpty
                }
            }

            is ApiResponse.Error -> {
                onError?.invoke(response.exception)
                ResponseState.Error(response.exception)
            }
        }
    }

    inner class SavedStateBindable<T>(
        private val savedStateHandle: SavedStateHandle,
        private val key: String,
        private val default: T,
        private val brTarget: Int? = null
    ) {
        operator fun getValue(thisRef: Any?, p: KProperty<*>): T {
            return savedStateHandle.get(key) ?: default
        }

        operator fun setValue(thisRef: Any?, p: KProperty<*>, value: T) {
            Log.i(TAG, "SAVED STATE HANDLE: KEY = $key; VALUE = $value")
            savedStateHandle.set(key, value)
            brTarget?.also { notifyPropertyChanged(it) }
        }
    }

    inner class LiveSavedStateBindable<T>(
        private val savedStateHandle: SavedStateHandle,
        private val key: String,
        private val brTarget: Int? = null
    ) {
        operator fun getValue(thisRef: Any?, p: KProperty<*>): LiveData<T> {
            val liveData = savedStateHandle.getLiveData<T>(key)
            return Transformations.map<T, T>(liveData) { value ->
                brTarget?.let { notifyPropertyChanged(brTarget) }
                Log.i(TAG, "SAVED STATE HANDLE: LIVE = $value")
                value
            }

        }
    }

    inner class DelegatedBindable<T> private constructor(
        private var value: T,
        private var bindingTarget: Array<Int>,
        private var isIdentity: Boolean,
        private val onValueChanged: ((T) -> Unit)? = null
    ) {

        constructor(
            value: T,
            bindingTarget: Int,
            isIdentity: Boolean = true,
            onValueChanged: ((T) -> Unit)? = null
        ) : this(value, arrayOf(bindingTarget), isIdentity, onValueChanged)

        constructor(
            value: T,
            vararg bindingTarget: Int,
            isIdentity: Boolean = true
        ) : this(value, bindingTarget.toTypedArray(), isIdentity)

        operator fun getValue(thisRef: Any?, p: KProperty<*>) = value

        operator fun setValue(thisRef: Any?, p: KProperty<*>, v: T) {
            value = if (isIdentity && v == value) return else v
            bindingTarget.forEach { notifyPropertyChanged(it) }
            onValueChanged?.invoke(v)
        }
    }

    companion object {
        private const val TAG = "VIEW_MODEL"
    }
}