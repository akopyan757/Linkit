package com.akopyan757.linkit.common.di

import androidx.room.Room
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.database.AppDatabase
import com.akopyan757.linkit.model.entity.PatternData
import com.akopyan757.urlparser.ElementType
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DatabaseModule {


    val module = module {

        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, Config.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single { get<AppDatabase>().urlLinkDao().apply { removeAll() } }
        single { get<AppDatabase>().storeLinkDao() }
        single { get<AppDatabase>().folderDao() }
        single {
            get<AppDatabase>().patternDao().apply {
                removeAll()
                insertOrUpdate(PatternData(base = true, pattern = "^.*/www.championat.com/.*",
                    titleElement = null, descriptionElement = null,
                    imageUrlElement = ElementType.LinkIconHref(type = "image/png", sizes = "48x48").getFormat()
                ))
                insertOrUpdate(PatternData(base = true, pattern = "^.*/yandex.ru/.*",
                    titleElement = null, descriptionElement = null,
                    imageUrlElement = ElementType.LinkIconHref(rel = "shortcut icon").getFormat()
                ))
                insertOrUpdate(PatternData(base = false,
                    pattern = "((http|https)://www.championat.com/[a-z]{2,16}/)article-.*",
                    titleElement = ElementType.MetaProperty("og:title").getFormat(),
                    descriptionElement = null,
                    imageUrlElement = ElementType.MetaProperty("og:image").getFormat()
                ))
            }
        }
    }
}