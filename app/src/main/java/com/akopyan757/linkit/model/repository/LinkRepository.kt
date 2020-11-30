package com.akopyan757.linkit.model.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.database.StoreLinkDao
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.PatternData
import com.akopyan757.linkit.model.entity.StoreLinkData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.urlparser.UrlParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.reflect.Type

class LinkRepository: BaseRepository(), KoinComponent {

    companion object {
        const val TAG = "LINK_REPOSITORY"
    }

    private val contentResolver: ContentResolver by inject()
    private val urlLinkDao: UrlLinkDao by inject()
    private val storeLinkDao: StoreLinkDao by inject()
    private val folderDao: FolderDao by inject()

    private val urlParser: UrlParser<PatternData, UrlLinkData> by inject()

    private val ioDispatcher: CoroutineDispatcher by lazy {
        Dispatchers.IO
    }

    init {
        initFolderAccount()
    }

    private fun initFolderAccount() {
        CoroutineScope(ioDispatcher).launch {
            //folderDao.removeAll()
            if (folderDao.getById(FolderData.GENERAL_FOLDER_ID) == null) {
                folderDao.insertOrUpdate(FolderData.generalFolder())
            }
            val type = object : TypeToken<List<PatternData>>() {}.type
            urlParser.loadPatternsFromJson(type)
        }
    }

    fun addNewLink(
        link: String,
        folderIds: List<Int>,
        title: String?,
        description: String?
    ) = call(ioDispatcher) {
        when {
            URLUtil.isHttpUrl(link) || URLUtil.isHttpsUrl(link) -> {
                parseHttpUrl(link, folderIds).also { linkData ->
                    title?.also { linkData.title = it }
                    description?.also { linkData.description = it }
                    urlLinkDao.insertOrUpdate(linkData)
                }
            }

            URLUtil.isContentUrl(link) -> {
                parseUri(link)?.also { linkData ->
                    storeLinkDao.insertOrUpdate(linkData)
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


    fun findFolderByRule(
        baseUrl: String
    ) = call(ioDispatcher) {
        folderDao.getByRuleUrl(baseUrl)
    }

    fun getAllUrlLinksByFolder(folderId: Int): LiveData<ApiResponse<List<UrlLinkData>>> = callLive(
        ioDispatcher
    ) {
        urlLinkDao.getLiveAll().map { list ->
            val data = list.filter {
                folderId in it.folderIds || folderId == FolderData.GENERAL_FOLDER_ID
            }

            ApiResponse.Success(data)
        }
    }

    fun getAllStoreLinksByFolder(folderId: Int): LiveData<ApiResponse<List<StoreLinkData>>> = callLive(
        ioDispatcher
    ) {
        storeLinkDao.getLiveAll().map { list ->
            val data = list.filter {
                folderId in it.folderIds || folderId == FolderData.GENERAL_FOLDER_ID
            }

            ApiResponse.Success(data)
        }
    }

    fun getAllFolders(): LiveData<ApiResponse<List<FolderData>>> = callLive(ioDispatcher) {
        folderDao.getLiveAll().map { list ->
            list.sortedBy { folder -> folder.order }.let { ApiResponse.Success(it) }
        }
    }

    private suspend fun parseHttpUrl(url: String, folderIds: List<Int> = emptyList()): UrlLinkData {
        return urlParser.parseUrl(url).apply {
            this.folderIds = folderIds
        }
    }

    @SuppressLint("Recycle")
    private fun parseUri(link: String): StoreLinkData? {

        val uri = Uri.parse(link)

        val cursor = contentResolver
                .query(uri, null, null, null, null)
                ?: return null

        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

        cursor.moveToFirst()

        val name = cursor.getString(nameIndex)
        val size = cursor.getLong(sizeIndex)
        val path = uri.path

        cursor.close()

        return StoreLinkData(uri = uri.toString(), name = name, size = size, path = path)
    }
}