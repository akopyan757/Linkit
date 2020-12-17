package com.akopyan757.linkit.model.repository

import android.content.Context
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.database.PatternDao
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.ParsePatternData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.linkit.common.utils.JsonPatternsParser
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.model.entity.PatternHostData
import com.akopyan757.urlparser.UrlParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

class LinkRepository: BaseRepository(), KoinComponent {

    companion object {
        const val TAG = "LINK_REPOSITORY"

        const val FILE_NAME = "pattern.json"
    }

    private val urlLinkDao: UrlLinkDao by inject()
    private val folderDao: FolderDao by inject()
    private val patternDao: PatternDao by inject()
    private val imageCache: ImageCache by inject()

    private val urlParser: UrlParser<ParsePatternData, PatternHostData, UrlLinkData> by inject()

    private val ioDispatcher: CoroutineDispatcher by lazy {
        Dispatchers.IO
    }

    init {
        CoroutineScope(ioDispatcher).launch {
            if (folderDao.getById(FolderData.GENERAL_FOLDER_ID) == null) {
                folderDao.insertOrUpdate(FolderData.generalFolder())
            }
            val context = get<Context>()
            val type = object : TypeToken<List<PatternHostData>>() {}.type
            val patterns = JsonPatternsParser.parse<PatternHostData>(context, FILE_NAME, type)

            patternDao.removeHostAll()
            patternDao.removeSpecifiedAll()

            patterns.forEach { hostPattern ->
                val id = patternDao.insertHostOrUpdate(hostPattern)

                hostPattern.patterns.forEach { specifiedData ->
                    specifiedData.hostId = id.toInt()
                    Log.i(TAG, "SpecifiedData = $specifiedData; $id")
                    patternDao.insertSpecifiedOrUpdate(specifiedData)
                }
            }
        }
    }

    fun addNewLink(
        link: String,
        folderId: Int?,
        title: String?,
        description: String?
    ) = call(ioDispatcher) {
        when {
            URLUtil.isHttpUrl(link) || URLUtil.isHttpsUrl(link) -> {
                val folder = folderId ?: FolderData.GENERAL_FOLDER_ID

                parseHttpUrl(link, folder)?.also { linkData ->

                    if (title != null) linkData.title = title
                    if (description != null) linkData.description = description

                    imageCache.saveImages(linkData)
                    urlLinkDao.insertOrUpdate(linkData)
                }
            }

            else -> null
        }
    }

    fun getDefaultInfoFromUrl(url: String): LiveData<ApiResponse<UrlLinkData>> = call(ioDispatcher) {
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            parseHttpUrl(url)
        } else {
            throw UrlIsNotValidException()
        }
    }

    fun addNewFolder(name: String) = call(ioDispatcher) {
        val existedFolder = folderDao.getByName(name)
        if (existedFolder != null) throw FolderExistsException()

        folderDao.insertOrUpdate(FolderData(name = name, order = 1))
        folderDao.getByName(name)
    }

    fun getUrlLinksByFolder(folderId: Int, isLive: Boolean = true): LiveData<ApiResponse<List<UrlLinkData>>> {
        return if (isLive) {
            callLive(ioDispatcher) {
                urlLinkDao.getLiveFromFolder(folderId)
                    .map { list -> ApiResponse.Success(list.convertUrls()) }
            }
        } else {
            call(ioDispatcher) { urlLinkDao.getByFolder(folderId).convertUrls() }
        }
    }

    private fun List<UrlLinkData>.convertUrls(): List<UrlLinkData> {
        return map { data ->
            data.apply {
                logoFileName = imageCache.getLogoName(data)
                contentFileName = imageCache.getContentName(data)
            }
        }.sortedBy { it._order }
    }

    fun getAllFolders(): LiveData<ApiResponse<List<FolderData>>> = callLive(ioDispatcher) {
        folderDao.getLiveAll().map { list ->
            list.sortedBy { folder -> folder.order }.let { ApiResponse.Success(it) }
        }
    }

    fun deleteUrls(ids: List<Long>): LiveData<ApiResponse<Unit>> = call(ioDispatcher) {
        ids.forEach { id -> urlLinkDao.removeById(id) }
    }

    fun deleteFolder(folderId: Int): LiveData<ApiResponse<Unit>> = call(ioDispatcher) {
        folderDao.removeById(folderId)
    }

    fun renameFolder(folderId: Int, newFolderName: String) = call(ioDispatcher) {
        val folder = folderDao.getById(folderId) ?: throw Exception("Folder not found")
        folder.name = newFolderName
        folderDao.insertOrUpdate(folder)
    }

    fun reorderLinks(orders: List<Pair<Long, Int>>) = call(ioDispatcher) {
        orders.forEach { (id, order) ->
            urlLinkDao.updateOrder(id, order)
        }
    }

    fun reorderFolders(orders: List<Pair<Int, Int>>) = call(ioDispatcher) {
        orders.forEach { (id, order) ->
            folderDao.updateOrder(id, order)
        }
    }

    private suspend fun parseHttpUrl(
        url: String,
        folderId: Int? = null
    ) = urlParser.parseUrl(url)?.also { data ->
        data.folderId = folderId ?: return@also
    }
}