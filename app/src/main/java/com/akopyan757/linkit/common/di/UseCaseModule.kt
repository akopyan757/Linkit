package com.akopyan757.linkit.common.di

import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.common.scheduler.AndroidMainSchedulerProvider
import com.akopyan757.linkit.common.scheduler.AndroidSchedulerProvider
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.auth.*
import com.akopyan757.linkit_domain.usecase.folder.*
import com.akopyan757.linkit_domain.usecase.urllink.*
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceIntentSignInUseCase
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceUserUserCase
import com.akopyan757.linkit_domain_android_ext.usecase.SignOutUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

object UseCaseModule {

    private const val IO_SCHEDULER = "IO_SCHEDULER"
    private const val MAIN_SCHEDULER = "MAIN_SCHEDULER"

    val module = module {
        single<SchedulerProvider>(named(IO_SCHEDULER)) { AndroidSchedulerProvider() }
        single<SchedulerProvider>(named(MAIN_SCHEDULER)) { AndroidMainSchedulerProvider() }

        single { (viewModel: BaseViewModel) ->
            SignOutUseCase(get(), get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            GetServiceUserUserCase(get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            GetServiceIntentSignInUseCase(get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            CreateUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            EmailVerificationUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            GetUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            LinkPasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ResetPasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            SignInWithEmailUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            UpdatePasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            CreateFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            DeleteFolderUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            GetAllFoldersUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFoldersChangesUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFolderChangesUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenFoldersUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            RenameFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ReorderFolderUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            CreateUrlLinkUseCase(get(), get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            DeleteUrlLinkUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenUrlLinkUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            ListenUrlLinkChangesUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            LoadHtmlCardsUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        single { (viewModel: BaseViewModel) ->
            MoveTopLinkUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
    }
}