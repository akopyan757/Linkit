package com.cheesecake.linkit.compose.di

import androidx.lifecycle.SavedStateHandle
import com.cheesecake.linkit.compose.Config.HANDLE_URL
import com.cheesecake.linkit.ui.auth.resetPassword.AuthResetPasswordViewModel
import com.cheesecake.linkit.ui.auth.setPassword.AuthSetPasswordViewModel
import com.cheesecake.linkit.ui.auth.signIn.AuthSignInViewModel
import com.cheesecake.linkit.ui.auth.signUp.AuthSignUpViewModel
import com.cheesecake.linkit.ui.auth.splash.AuthSplashViewModel
import com.cheesecake.linkit.ui.auth.start.AuthStartViewModel
import com.cheesecake.linkit.ui.auth.updatePassword.AuthUpdatePasswordViewModel
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