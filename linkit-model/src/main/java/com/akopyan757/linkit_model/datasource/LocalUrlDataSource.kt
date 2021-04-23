package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_model.database.UrlLinkDao
import com.akopyan757.linkit_model.database.data.UrlLinkData
import com.akopyan757.linkit_model.mapper.Mapper
import io.reactivex.Completable
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

    override fun getNewOrderValue(): Int {
        return urlLinkDao.getMaxOrder().plus(ONE)
    }

    override fun updateAllUrlLinks(links: List<UrlLinkEntity>) = Completable.fromCallable {
        val data = links.map(mapper::secondToFirst)
        urlLinkDao.updateAll(data)
    }

    override fun updateUrlLink(link: UrlLinkEntity) = Completable.fromCallable {
        val data = link.let(mapper::secondToFirst)
        urlLinkDao.insertOrUpdate(data)
    }

    override fun updateLinkOrder(linkId: String, order: Int) = Completable.fromCallable {
        urlLinkDao.updateLinkOrder(linkId, order)
    }

    override fun removeUrlLinkById(linkId: String) = Completable.fromCallable {
        urlLinkDao.removeById(linkId)
    }

    companion object {
        private const val ONE = 1
        private const val EMPTY = ""
        private val NULL = null
    }

}