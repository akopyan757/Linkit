package com.akopyan757.linkit_model.datasource

import android.util.Log
import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_model.parser.IHtmlParser


class LoadHtmlCardsDataSource(
    private val htmlParser: IHtmlParser
): ILoadHtmlCardsDataSource {

    override fun loadCard(resourceUrl: String): HtmlLinkCardEntity {
        val htmlTags = htmlParser.parseHeadTagsFromResource(resourceUrl)
        Log.i("LoadHtmlCardsDataSource", htmlTags.toString())
        return listOf(
            htmlTags.twitterHtmlTags.toCard(),
            htmlTags.openGraphHtmlTags.toCard(),
            htmlTags.additionalHtmlTags.toCard()
        ).maxByOrNull { card -> card.rating() } ?: HtmlLinkCardEntity()
    }
}