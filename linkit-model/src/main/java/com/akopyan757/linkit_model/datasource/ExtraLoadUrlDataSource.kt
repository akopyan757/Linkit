package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.IExtraLoadUrlDataSource
import com.akopyan757.linkit_model.parser.IHtmlParser

class ExtraLoadUrlDataSource(
    private val htmlParser: IHtmlParser
): IExtraLoadUrlDataSource {

    override fun loadExtraData(entity: UrlLinkEntity): UrlLinkEntity {
        val htmlTags = htmlParser.parseHeadTagsFromResource(entity.url)
        val card = htmlTags.openGraphHtmlTags.toCard()
        return UrlLinkEntity(
            entity.id, entity.url, card.title ?: "", card.description ?: "",
            card.photoUrl, entity.logoUrl, entity.folderId, entity.order
        )
    }
}