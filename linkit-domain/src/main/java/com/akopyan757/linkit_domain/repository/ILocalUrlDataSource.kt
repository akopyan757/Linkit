package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import io.reactivex.Observable

interface ILocalUrlDataSource {
    fun listenUrlLink(): Observable<List<UrlLinkEntity>>
    fun listenUrlLinkFromFolder(folderId: String): Observable<List<UrlLinkEntity>>
    fun checkExistUrlLink(linkId: String): Boolean
    fun getAllUrlLinks(): List<UrlLinkEntity>
    fun createUrlLinkInstance(url: String, folderId: String?): UrlLinkEntity
    fun getNewOrderValue(): Int
    fun updateAllUrlLinks(links: List<UrlLinkEntity>)
    fun updateUrlLink(link: UrlLinkEntity)
    fun removeUrlLinkById(linkId: String)
}