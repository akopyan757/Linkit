package com.akopyan757.base.viewmodel

import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KProperty

abstract class BaseViewModel: ViewModel(), BaseStateObservable {

    private val compositeDisposable = CompositeDisposable()
    private val mAction = MutableLiveData<Int>()
    private val mCallbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    infix fun emitAction(code: Int) {
        mAction.value = code
    }

    fun getLiveAction(): LiveData<Int> = mAction

    fun getCompositeDisposable() = compositeDisposable

    override fun getCallback(): PropertyChangeRegistry {
        return mCallbacks
    }

    inline fun <reified T> KoinComponent.injectUseCase(
        qualifier: Qualifier? = null
    ): Lazy<T> = getKoin().inject(qualifier) { parametersOf(this@BaseViewModel) }

    inner class DB<T> private constructor(
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "VIEW_MODEL"
    }
}