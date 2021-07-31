package com.cheesecake.linkit.compose

import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier

abstract class BaseViewModel: ViewModel(), KoinComponent {

    private val compositeDisposable = CompositeDisposable()

    fun getCompositeDisposable() = compositeDisposable

    inline fun <reified T> KoinComponent.injectUseCase(
        qualifier: Qualifier? = null
    ): Lazy<T> = getKoin().inject(qualifier) { parametersOf(this@BaseViewModel) }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}