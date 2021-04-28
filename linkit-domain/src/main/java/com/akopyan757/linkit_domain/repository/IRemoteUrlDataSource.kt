package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IRemoteUrlDataSource {
    fun loadUrlLinks(): Single<List<UrlLinkEntity>>
    fun listenUrlLinksChanges(): Observable<DataChange<UrlLinkEntity>>
    fun changeUrlCollapse(linkId: String, collapse: Boolean): Completable
    fun assignLinksToFolder(folderId: String, links: List<String>): Completable
    fun createOrUpdateUrlLink(data: UrlLinkEntity): Single<UrlLinkEntity>
    fun deleteUrlLinks(linkIds: List<String>): Completable
    fun setOrderForUrlLink(linkId: String, order: Int): Single<Int>
}