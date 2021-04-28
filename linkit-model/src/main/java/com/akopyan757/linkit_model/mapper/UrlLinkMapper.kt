package com.akopyan757.linkit_model.mapper

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.entity.UrlLinkAppEntity
import com.akopyan757.linkit_model.database.data.UrlLinkAppData
import com.akopyan757.linkit_model.database.data.UrlLinkData

class UrlLinkMapper: Mapper<UrlLinkData, UrlLinkEntity> {

    override fun firstToSecond(data: UrlLinkData): UrlLinkEntity {
        val type = data.type?.let { value -> UrlLinkEntity.Type.fromValue(value) } ?: UrlLinkEntity.Type.DEFAULT
        val app = data.app?.let { app -> UrlLinkAppEntity(app.appId, app.appName, app.url) }
        return UrlLinkEntity(
            data.id, data.url, data.title, data.description, data.photoUrl,
            data.folderId, data.site, type, app, data._order, data.collapsed
        )
    }

    override fun secondToFirst(data: UrlLinkEntity): UrlLinkData {
        val type = data.type.takeUnless { type -> type == UrlLinkEntity.Type.DEFAULT }?.value
        val app = data.app?.let { app -> UrlLinkAppData(app.appId, app.appName, app.url) }
        return UrlLinkData(
            data.id, data.url, data.title, data.description, data.photoUrl,
            data.folderId, data.site, type, data.order, data.collapsed, app
        )
    }
}