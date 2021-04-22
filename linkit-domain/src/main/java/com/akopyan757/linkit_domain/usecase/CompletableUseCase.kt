package com.akopyan757.linkit_domain.usecase

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

abstract class CompletableUseCase(
    protected val compositeDisposable: CompositeDisposable
): UseCase<Completable, CompletableUseCase.NothingParams>() {

    fun execute(onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(NothingParams).subscribe(
            onSuccess ?: defaultOnNext(),
            onError ?: defaultOnError()
        )

        compositeDisposable.add(disposable)
    }

    object NothingParams: UseCase.Params()
}