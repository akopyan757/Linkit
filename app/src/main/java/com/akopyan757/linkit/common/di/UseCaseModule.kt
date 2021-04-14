package com.akopyan757.linkit.common.di

import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.common.AndroidSchedulerProvider
import com.akopyan757.linkit.viewmodel.AuthStartViewModel
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.folder.*
import com.akopyan757.linkit_domain.usecase.urllink.*
import org.koin.dsl.module

object UseCaseModule {

    val module = module {
        single<SchedulerProvider> {
            AndroidSchedulerProvider()
        }
        single { (viewModel: BaseViewModel) ->
            CreateFolderUseCase(get(), get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            DeleteFolderUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            GetAllFoldersUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFoldersChangesUseCase(get(), get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFolderChangesUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFoldersUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            RenameFolderUseCase(get(), get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ReorderFolderUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            CreateUrlLinkUseCase(get(), get(), get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            DeleteUrlLinkUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenUrlLinkUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenUrlLinkChangesUseCase(get(), get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            LoadHtmlCardsUseCase(get(), get(), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            MoveTopLinkUseCase(get(), get(), get(), viewModel.getCompositeDisposable())
        }
    }
}