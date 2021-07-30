package com.example.linkit_app.compose.di

import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.auth.*
import com.akopyan757.linkit_domain.usecase.folder.*
import com.akopyan757.linkit_domain.usecase.urllink.*
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceIntentSignInUseCase
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceUserUserCase
import com.akopyan757.linkit_domain_android_ext.usecase.SignOutUseCase
import com.example.linkit_app.compose.BaseViewModel
import com.example.linkit_app.compose.scheduler.AndroidMainSchedulerProvider
import com.example.linkit_app.compose.scheduler.AndroidSchedulerProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

object UseCaseModule {

    private const val IO_SCHEDULER = "IO_SCHEDULER"
    private const val MAIN_SCHEDULER = "MAIN_SCHEDULER"

    val module = module {
        single<SchedulerProvider>(named(IO_SCHEDULER)) { AndroidSchedulerProvider() }
        single<SchedulerProvider>(named(MAIN_SCHEDULER)) { AndroidMainSchedulerProvider() }

        factory { (viewModel: BaseViewModel) ->
            SignOutUseCase(get(), get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            GetServiceUserUserCase(get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            GetServiceIntentSignInUseCase(get(), get(named(MAIN_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            CreateUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            EmailVerificationUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            GetUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            LinkPasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            ResetPasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            SignInWithEmailUserUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            UpdatePasswordUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            CreateFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            DeleteFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            ListenFoldersChangesUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            RenameFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            ReorderFolderUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            CreateUrlLinkUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            DeleteUrlLinkUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            UpdateAssignUrlLinkUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            ListenUrlLinkUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            GetUrlLinkListUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            LoadHtmlCardsUseCase(get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
        factory { (viewModel: BaseViewModel) ->
            MoveTopLinkUseCase(get(), get(), get(named(IO_SCHEDULER)), viewModel.getCompositeDisposable())
        }
    }
}