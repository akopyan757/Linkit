package com.example.linkit_app.compose.di

import androidx.lifecycle.SavedStateHandle
import com.example.linkit_app.compose.Config.HANDLE_URL
import com.example.linkit_app.ui.auth.resetPassword.AuthResetPasswordViewModel
import com.example.linkit_app.ui.auth.setPassword.AuthSetPasswordViewModel
import com.example.linkit_app.ui.auth.signIn.AuthSignInViewModel
import com.example.linkit_app.ui.auth.signUp.AuthSignUpViewModel
import com.example.linkit_app.ui.auth.splash.AuthSplashViewModel
import com.example.linkit_app.ui.auth.start.AuthStartViewModel
import com.example.linkit_app.ui.auth.updatePassword.AuthUpdatePasswordViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ViewModelModule {

    val module = module {
        single(named(HANDLE_URL)) { SavedStateHandle() }

        factory { CompositeDisposable() }

        viewModel { AuthSplashViewModel() }
        viewModel { AuthStartViewModel() }
        viewModel { AuthResetPasswordViewModel() }
        viewModel { AuthSetPasswordViewModel() }
        viewModel { AuthUpdatePasswordViewModel() }
        viewModel { AuthSignUpViewModel() }
        viewModel { AuthSignInViewModel() }
    }
}