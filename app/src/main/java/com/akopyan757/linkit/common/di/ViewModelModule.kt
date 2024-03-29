package com.akopyan757.linkit.common.di

import androidx.lifecycle.SavedStateHandle
import com.akopyan757.linkit.common.Config.HANDLE_URL
import com.akopyan757.linkit.viewmodel.*
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ViewModelModule {

    val module = module {
        single(named(HANDLE_URL)) { SavedStateHandle() }

        factory { CompositeDisposable() }

        viewModel { LinkViewModel() }
        viewModel { (url: String) -> LinkCreateUrlViewModel(url) }
        viewModel { FolderViewModel() }
        viewModel { FolderCreateViewModel() }
        viewModel { AuthSignInViewModel() }
        viewModel { AuthSignUpViewModel() }
        viewModel { AuthStartViewModel() }
        viewModel { AuthForgotPasswordViewModel() }
        viewModel { AuthSplashViewModel() }
        viewModel { ProfileViewModel() }
        viewModel { ProfileSetPasswordViewModel() }
        viewModel { ProfileUpdatePasswordViewModel() }
        viewModel { (observable: FolderObservable) -> FolderRenameViewModel(observable) }
        viewModel { (observable: LinkObservable) -> PreviewUrlViewModel(observable) }
    }
}