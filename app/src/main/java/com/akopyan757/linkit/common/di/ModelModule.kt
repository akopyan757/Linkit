package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.model.database.PatternDao
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.model.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.urlparser.IUrlParser
import com.akopyan757.urlparser.UrlParser
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModelModule {

    val module = module {

        scope(named<MainActivity>()) {
            scoped<IUrlParser<UrlLinkData>> { UrlParser(get<PatternDao>(), UrlLinkData.Factory()) }
            scoped { LinkRepository() }
        }

        single { AuthRepository() }
        single { FirebaseEmailAuthorizationService() }
    }
}