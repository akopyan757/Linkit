package com.akopyan757.linkit_model.mapper

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_model.database.data.UrlLinkData

class UrlLinkMapper: Mapper<UrlLinkData, UrlLinkEntity> {

    override fun firstToSecond(data: UrlLinkData): UrlLinkEntity {
        return UrlLinkEntity(
            data.id, data.url, data.title, data.description, data.photoUrl, data.logoUrl,
            data.folderId, data._order
        )
    }

    override fun secondToFirst(data: UrlLinkEntity): UrlLinkData {
        return UrlLinkData(
            data.id, data.url, data.title, data.description, data.photoUrl, data.logoUrl,
            data.folderId, data.order
        )
    }
}