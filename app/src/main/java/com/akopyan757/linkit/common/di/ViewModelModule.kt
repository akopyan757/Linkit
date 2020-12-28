package com.akopyan757.linkit.common.di

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import com.akopyan757.linkit.common.Config.HANDLE_URL
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ViewModelModule {

    val module = module {
        single(named(HANDLE_URL)) { SavedStateHandle() }
        single { (activity: Activity) -> FirebaseEmailAuthorizationService(activity) }

        viewModel { LinkViewModel() }
        viewModel { (url: String) -> LinkCreateUrlViewModel(url) }
        viewModel { FolderViewModel() }
        viewModel { FolderCreateViewModel() }
        viewModel { (folderId: Int) -> PageViewModel(folderId) }
        viewModel { AuthSignInViewModel() }
        viewModel { AuthSignUpViewModel() }
        viewModel { AuthForgotPasswordViewModel() }
        viewModel { ProfileViewModel() }
    }
}