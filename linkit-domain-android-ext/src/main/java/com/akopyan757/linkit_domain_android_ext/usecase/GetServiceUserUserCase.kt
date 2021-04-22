package com.akopyan757.linkit_domain_android_ext.usecase

import android.content.Intent
import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import io.reactivex.disposables.CompositeDisposable

class GetServiceUserUserCase(
    private val authIntentService: IAuthIntentDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleWithParamsUseCase<UserEntity, GetServiceUserUserCase.Params>(compositeDisposable) {

    override fun launch() = authIntentService.getServiceUser(parameters.data)
        .observeOn(schedulerProvider.mainThread)

    data class Params(val data: Intent?): UseCase.Params()
}