package com.akopyan757.linkit_domain_android_ext.usecase

import android.content.Intent
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleUseCase
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class GetServiceIntentSignInUseCase(
    private val authIntentService: IAuthIntentDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleUseCase<Intent>(schedulerProvider, compositeDisposable) {

    override fun launch(): Single<Intent> = authIntentService.getServiceSignInIntent()
}