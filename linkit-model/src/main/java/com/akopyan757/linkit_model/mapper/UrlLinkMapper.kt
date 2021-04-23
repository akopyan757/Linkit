package com.akopyan757.linkit_model.mapper

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_model.database.data.UrlLinkData

class UrlLinkMapper: Mapper<UrlLinkData, UrlLinkEntity> {

    override fun firstToSecond(data: UrlLinkData): UrlLinkEntity {
        val type = data.type?.let { value -> UrlLinkEntity.Type.fromValue(value) } ?: UrlLinkEntity.Type.DEFAULT
        return UrlLinkEntity(
            data.id, data.url, data.title, data.description, data.photoUrl, data.logoUrl,
            data.folderId, data.site, type, data._order
        )
    }

    override fun secondToFirst(data: UrlLinkEntity): UrlLinkData {
        val type = data.type.takeUnless { type -> type == UrlLinkEntity.Type.DEFAULT }?.value
        return UrlLinkData(
            data.id, data.url, data.title, data.description, data.photoUrl, data.logoUrl,
            data.folderId, data.site, type, data.order
        )
    }
}