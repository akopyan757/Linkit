package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.model.database.PatternDao
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.urlparser.IUrlParser
import com.akopyan757.urlparser.UrlParser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ModelModule {

    val module = module {
        single { androidContext().contentResolver }

        single<IUrlParser<UrlLinkData>> { UrlParser(get<PatternDao>(), UrlLinkData.Factory()) }

        single { LinkRepository() }

    }
}