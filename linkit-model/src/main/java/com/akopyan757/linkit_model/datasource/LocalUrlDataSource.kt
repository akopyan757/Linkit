package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_model.database.UrlLinkDao
import com.akopyan757.linkit_model.database.data.UrlLinkData
import com.akopyan757.linkit_model.mapper.Mapper
import io.reactivex.Observable
import java.util.*

class LocalUrlDataSource(
    private val urlLinkDao: UrlLinkDao,
    private val mapper: Mapper<UrlLinkData, UrlLinkEntity>
): ILocalUrlDataSource {

    override fun checkExistUrlLink(linkId: String): Boolean {
        return urlLinkDao.checkExistLink(linkId)
    }

    override fun listenUrlLink(): Observable<List<UrlLinkEntity>> {
        return urlLinkDao.getAllLive().map { urlLinkList ->
            urlLinkList.map(mapper::firstToSecond)
        }.toObservable()
    }

    override fun listenUrlLinkFromFolder(folderId: String): Observable<List<UrlLinkEntity>> {
        return urlLinkDao.getByFolderLive(folderId).map { urlLinkList ->
            urlLinkList.map(mapper::firstToSecond)
        }.toObservable()
    }

    override fun getAllUrlLinks(): List<UrlLinkEntity> {
        return urlLinkDao.getAll().map(mapper::firstToSecond)
    }

    override fun createUrlLinkInstance(url: String, folderId: String?): UrlLinkEntity {
        val id = UUID.randomUUID().toString()
        val order = urlLinkDao.getMaxOrder().plus(ONE)
        return UrlLinkEntity(id, url, EMPTY, EMPTY, NULL, NULL, folderId, order)
    }

    override fun getNewOrderValue(): Int {
        return urlLinkDao.getMaxOrder().plus(ONE)
    }

    override fun updateAllUrlLinks(links: List<UrlLinkEntity>) {
        val data = links.map(mapper::secondToFirst)
        urlLinkDao.updateAll(data)
    }

    override fun updateUrlLink(link: UrlLinkEntity) {
        val data = link.let(mapper::secondToFirst)
        urlLinkDao.insertOrUpdate(data)
    }

    override fun removeUrlLinkById(linkId: String) {
        urlLinkDao.removeById(linkId)
    }

    companion object {
        private const val ONE = 1
        private const val EMPTY = ""
        private val NULL = null
    }

}