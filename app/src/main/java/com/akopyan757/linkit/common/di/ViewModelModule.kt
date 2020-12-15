package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    val module = module {
        viewModel { LinkViewModel() }
        viewModel { (url: String) -> LinkCreateUrlViewModel(url) }
        viewModel { FolderViewModel() }
        viewModel { FolderCreateViewModel() }
        viewModel { (folderId: Int) -> PageViewModel(folderId) }
    }
}