package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class CreateUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<CreateUrlLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return Single.fromCallable {
            parameters.urlLinkEntity.apply {
                id = UUID.randomUUID().toString()
                order = localDataSource.getNewOrderValue()
                folderId = parameters.folderId
            }
        }.flatMap { entity ->
            remoteDataSource.createOrUpdateUrlLink(entity)
        }.flatMapCompletable { urlEntity ->
            localDataSource.updateUrlLink(urlEntity)
                .subscribeOn(schedulerProvider.ioThread)
        }
        .subscribeOn(schedulerProvider.ioThread)
        .observeOn(schedulerProvider.mainThread)
    }

    data class Params(
        val folderId: String?,
        val urlLinkEntity: UrlLinkEntity
    ): UseCase.Params()
}