package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.*
import com.akopyan757.linkit_model.database.data.FolderData
import com.akopyan757.linkit_model.database.data.UrlLinkData
import com.akopyan757.linkit_model.datasource.*
import com.akopyan757.linkit_model.mapper.FolderMapper
import com.akopyan757.linkit_model.mapper.Mapper
import com.akopyan757.linkit_model.mapper.UrlLinkMapper
import com.akopyan757.linkit_model.parser.HtmlParser
import com.akopyan757.linkit_model.parser.IHtmlParser
import com.google.firebase.firestore.CollectionReference
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModelModule {

    private const val MAPPER_URL = "url_link"
    private const val MAPPER_FOLDER = "folder"

    val module = module {
        single { AuthRepository() }

        single<IHtmlParser> { HtmlParser() }

        single<Mapper<UrlLinkData, UrlLinkEntity>>(named(MAPPER_URL)) { UrlLinkMapper() }
        single<Mapper<FolderData, FolderEntity>>(named(MAPPER_FOLDER)) { FolderMapper() }

        single<IRemoteUrlDataSource> {
            RemoteUrlLinkDataSource(get(named(Config.LINKS)), get())
        }
        single<IRemoteFolderDataSource> {
            RemoteFolderDataSource(get(named(Config.LINKS)), get(), get())
        }
        single<ILocalUrlDataSource> {
            LocalUrlDataSource(get(), get(named(MAPPER_URL)))
        }
        single<ILocalFolderDataSource> {
            LocalFolderDataSource(get(), get(named(MAPPER_FOLDER)))
        }
        single<IExtraLoadUrlDataSource> {
            ExtraLoadUrlDataSource(get())
        }
        single<ILoadHtmlCardsDataSource> {
            LoadHtmlCardsDataSource(get())
        }
    }
}