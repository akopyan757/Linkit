package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.entity.UrlLinkGoogleAppEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CreateUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<CreateUrlLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return Single.fromCallable {
            localDataSource.createUrlLinkInstance(parameters.url).let { entity ->
                UrlLinkEntity(
                    entity.id, entity.url, parameters.title, parameters.description,
                    parameters.photoUrl, null, parameters.folderId, parameters.site,
                    parameters.type, parameters.googleAppEntity, entity.order
                )
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
        val url: String,
        val folderId: String?,
        val title: String,
        val description: String,
        val site: String?,
        val type: UrlLinkEntity.Type,
        val photoUrl: String?,
        val googleAppEntity: UrlLinkGoogleAppEntity?
    ): UseCase.Params()
}