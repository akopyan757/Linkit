package com.akopyan757.linkit.viewmodel

import android.content.Intent
import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.view.service.IAuthorizationService
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthStartViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val authService: IAuthorizationService by inject()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)

    fun requestSignInWithService(data: Intent?) = requestConvert(
        request = authRepository.signInWithData(data),
        onLoading = { isProgress = true },
        onSuccess = { firebaseUser ->
            isProgress = false
            return@requestConvert firebaseUser.uid
        }, onError = {
            isProgress = false
        }
    )

    fun getSignInIntent() = authService.getSignInIntent()
}