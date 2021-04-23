package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UrlLinkEntity

interface ILoadHtmlCardsDataSource {
    fun loadCard(resourceUrl: String): UrlLinkEntity
}