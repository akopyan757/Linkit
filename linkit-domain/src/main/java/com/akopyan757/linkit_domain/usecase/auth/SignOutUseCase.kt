package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.*
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class SignOutUseCase(
    private val authDataSource: IAuthDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase(
    schedulerProvider, compositeDisposable
) {

    override fun launch() = Completable.fromAction {
        authDataSource.signOut()
    }
}