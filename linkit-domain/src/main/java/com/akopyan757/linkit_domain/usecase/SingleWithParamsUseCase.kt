package com.akopyan757.linkit_domain.usecase

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class SingleWithParamsUseCase<T, P : UseCase.Params>(
    private val compositeDisposable: CompositeDisposable
): UseCase<Single<T>, P>() {

    fun execute(
        params: P,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        val disposable = super.execute(params)
            .subscribe(
                onSuccess ?: defaultOnNextWithParams<T>(),
                onError ?: defaultOnError()
            )

        compositeDisposable.add(disposable)
    }
}