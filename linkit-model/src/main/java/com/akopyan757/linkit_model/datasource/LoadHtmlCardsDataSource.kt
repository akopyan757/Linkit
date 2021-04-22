package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_model.parser.IHtmlParser


class LoadHtmlCardsDataSource(
    private val htmlParser: IHtmlParser
): ILoadHtmlCardsDataSource {

    override fun loadCard(resourceUrl: String): HtmlLinkCardEntity {
        val htmlTags = htmlParser.parseHeadTagsFromResource(resourceUrl)
        return listOf(
            htmlTags.openGraphHtmlTags.toCard(),
            htmlTags.twitterHtmlTags.toCard(),
            htmlTags.additionalHtmlTags.toCard()
        ).maxByOrNull { card -> card.rating() } ?: HtmlLinkCardEntity()
    }
}