package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.disposables.CompositeDisposable

class CreateUserUseCase(
    private val authDataSource: IAuthDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleWithParamsUseCase<UserEntity, CreateUserUseCase.Params>(compositeDisposable) {

    override fun launch() = authDataSource.createUser(parameters.email, parameters.password)
        .subscribeOn(schedulerProvider.ioThread)
        .observeOn(schedulerProvider.mainThread)

    data class Params(val email: String, val password: String): UseCase.Params()
}