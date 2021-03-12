package com.akopyan757.linkit.viewmodel

import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.model.repository.AuthRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthSplashViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    fun getUserRequest() = requestConvert(
            request = authRepository.getUser(),
            onSuccess = { firebaseUser -> firebaseUser }
    )
}