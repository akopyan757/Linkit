package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ILocalUrlDataSource {
    fun listenUrlLink(): Observable<List<UrlLinkEntity>>
    fun listenUrlLinkFromFolder(folderId: String): Observable<List<UrlLinkEntity>>
    fun assignLinksToFolder(folderId: String, links: List<String>): Completable
    fun changeUrlCollapse(linkId: String, collapse: Boolean): Completable
    fun checkExistUrlLink(linkId: String): Boolean
    fun getAllUrlLinks(): List<UrlLinkEntity>
    fun getNewOrderValue(): Int
    fun updateAllUrlLinks(links: List<UrlLinkEntity>): Completable
    fun updateUrlLink(link: UrlLinkEntity): Completable
    fun updateLinkOrder(linkId: String, order: Int): Completable
    fun removeUrlLinkByIds(linkId: List<String>): Completable
}