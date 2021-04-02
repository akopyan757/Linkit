package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.model.parser.HtmlParser
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.model.repository.FolderRepository
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.MainActivity

import org.koin.dsl.module

object ModelModule {

    val module = module {
        single { HtmlParser() }
        single { LinkRepository() }
        single { FolderRepository() }
        single { AuthRepository() }
    }
}