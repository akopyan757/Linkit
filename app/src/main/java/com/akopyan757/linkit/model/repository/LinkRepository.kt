package com.akopyan757.linkit.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.DataChange
import com.akopyan757.linkit.model.entity.HtmlLinkCard
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.linkit.model.parser.HtmlParser
import com.akopyan757.linkit.model.parser.tags.HtmlTags
import com.akopyan757.linkit.model.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.util.*

class LinkRepository: BaseRepository(), KoinComponent {

    private val urlLinkDao: UrlLinkDao by inject()
    private val htmlParser: HtmlParser by inject()
    private val remoteDataSource: RemoteDataSource by inject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    fun createLink(url: String, folderId: String?) = wrapActionIO {
        if (!FormatUtils.isUrl(url))
            throw UrlIsNotValidException()
        val id = UUID.randomUUID().toString()
        val data = UrlLinkData(id, url, folderId=folderId)
        remoteDataSource.createOrUpdateUrlLink(data)
        loadExtraDataForUrlData(data)
    }

    fun moveLinkToTop(urlLinkId: String) = launchIO {
        urlLinkDao.getById(urlLinkId) ?: throw Exception("Url link not found")
        val newOrder = urlLinkDao.getMaxOrder().plus(1)
        remoteDataSource.setOrderForUrlLink(urlLinkId, newOrder)
    }

    fun listenUrlDataFromCache(folderId: String?): LiveData<List<UrlLinkData>> {
        return if (folderId == null) {
            urlLinkDao.getAllLive()
        } else {
            urlLinkDao.getByFolderLive(folderId)
        }.asLiveIO()
    }

    fun listenRemoteData(): LiveData<Unit> = liveData(coroutineDispatcher) {
        remoteDataSource.loadUrlLinks().also { firstData ->
            saveChangesToCache(DataChange.Initialized(firstData))
            emit(Unit)
        }
        val liveData = remoteDataSource.listenUrlLinksChanges().map { dataChange ->
            runBlocking(coroutineDispatcher) {
                saveChangesToCache(dataChange)
            }
        }
        emitSource(liveData)
    }

    fun loadAllCards(resourceUrl: String) = wrapActionIOWithResult {
        HtmlTags.Type.values().map { type -> loadHtmlCard(resourceUrl, type) }
    }

    private fun saveChangesToCache(urlLinkDataChange: DataChange<UrlLinkData>) {
        when (urlLinkDataChange) {
            is DataChange.Initialized -> urlLinkDao.updateAll(urlLinkDataChange.data)
            is DataChange.Added -> urlLinkDao.insertOrUpdate(urlLinkDataChange.data)
            is DataChange.Modified -> urlLinkDao.insertOrUpdate(urlLinkDataChange.data)
            is DataChange.Deleted -> urlLinkDao.removeById(urlLinkDataChange.data.id)
            else -> {}
        }
    }

    private fun loadExtraDataForUrlData(data: UrlLinkData) = launchIO {
        val card = loadHtmlCard(data.url, HtmlTags.Type.OpenGraph)
        data.title = card.title ?: ""
        data.description = card.description ?: ""
        data.photoUrl = card.photoUrl
        remoteDataSource.createOrUpdateUrlLink(data)
    }

    private fun loadHtmlCard(
        resourceUrl: String,
        cardType: HtmlTags.Type
    ): HtmlLinkCard {
        val tags = htmlParser.parseHeadTagsFromResource(resourceUrl)
        return HtmlLinkCard.getCard(tags, cardType).apply {
            title = title?.let { FormatUtils.highlightWithoutLink(it) }
            description = description?.let { FormatUtils.highlightWithoutLink(it) }
        }
    }
}