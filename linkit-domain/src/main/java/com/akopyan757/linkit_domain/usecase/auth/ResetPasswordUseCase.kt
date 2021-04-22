package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ResetPasswordUseCase(
    private val authDataSource: IAuthDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ResetPasswordUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return authDataSource.resetPassword(parameters.email)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val email: String): UseCase.Params()
}