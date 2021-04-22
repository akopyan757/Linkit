package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.*
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class EmailVerificationUseCase(
    private val authDataSource: IAuthDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase(compositeDisposable) {

    override fun launch(): Completable {
        return authDataSource.emailVerification()
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }
}