package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class LoadHtmlCardsUseCase(
    private val loadHtmlCardsDataSource: ILoadHtmlCardsDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
) : SingleWithParamsUseCase<List<HtmlLinkCardEntity>, LoadHtmlCardsUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Single.fromCallable {
        loadHtmlCardsDataSource.loadCards(parameters.resourceUrl)
    }

    data class Params(val resourceUrl: String): UseCase.Params()
}