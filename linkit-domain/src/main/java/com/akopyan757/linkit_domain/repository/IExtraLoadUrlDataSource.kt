package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UrlLinkEntity

interface IExtraLoadUrlDataSource {
    fun loadExtraData(entity: UrlLinkEntity): UrlLinkEntity
}