package com.cheesecake.linkit.compose.di

import android.content.Context
import android.content.SharedPreferences
import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.*
import com.akopyan757.linkit_model.database.data.FolderData
import com.akopyan757.linkit_model.database.data.UrlLinkData
import com.akopyan757.linkit_model.datasource.*
import com.akopyan757.linkit_model.mapper.FolderMapper
import com.akopyan757.linkit_model.mapper.Mapper
import com.akopyan757.linkit_model_auth.mapper.MapperDirect
import com.akopyan757.linkit_model.mapper.UrlLinkMapper
import com.akopyan757.linkit_model.parser.HtmlParser
import com.akopyan757.linkit_model.parser.IHtmlParser
import com.akopyan757.linkit_model_auth.cache.ITokenCache
import com.akopyan757.linkit_model_auth.cache.TokenCache
import com.akopyan757.linkit_model_auth.datasource.AuthFirebaseDataSource
import com.akopyan757.linkit_model_auth.mapper.UserMapper
import com.cheesecake.linkit.compose.Config
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModelModule {

    private const val KEY_SHARED = "SHARED_PREFS"
    private const val MAPPER_URL = "url_link"
    private const val MAPPER_FOLDER = "folder"
    private const val MAPPER_USER = "user"

    val module = module {

        single<SharedPreferences> {
            androidContext().getSharedPreferences(KEY_SHARED, Context.MODE_PRIVATE)
        }

        single<ITokenCache> { TokenCache(get()) }

        single<IHtmlParser> { HtmlParser() }

        single<Mapper<UrlLinkData, UrlLinkEntity>>(named(MAPPER_URL)) { UrlLinkMapper() }
        single<Mapper<FolderData, FolderEntity>>(named(MAPPER_FOLDER)) { FolderMapper() }
        single<MapperDirect<FirebaseUser, UserEntity>>(named(MAPPER_USER)) { UserMapper() }

        single<IRemoteUrlDataSource> {
            RemoteUrlLinkDataSource(get(), get(named(Config.LINKS)), get())
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
        single<ILoadHtmlCardsDataSource> {
            LoadHtmlCardsDataSource(get())
        }
        single<IAuthDataSource> {
            AuthFirebaseDataSource(get(), get(named(MAPPER_USER)))
        }
    }
}