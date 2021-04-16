package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CreateUserUseCase(
    private val authDataSource: IAuthDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleWithParamsUseCase<UserEntity, CreateUserUseCase.Params>(
    schedulerProvider, compositeDisposable
) {

    override fun launch() = authDataSource.createUser(parameters.email, parameters.password)

    data class Params(val email: String, val password: String): UseCase.Params()
}