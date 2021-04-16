package com.akopyan757.linkit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import org.koin.core.KoinComponent

class AuthSplashViewModel: BaseViewModel(), KoinComponent {

    private val getUser: GetUserUseCase by injectUseCase()

    private val successUserLive = MutableLiveData<UserEntity>()
    private val throwableLive = MutableLiveData<Throwable>()

    fun getSuccessUserLive(): LiveData<UserEntity> {
        return successUserLive
    }

    fun getThrowableLive(): LiveData<Throwable> {
        return throwableLive
    }

    fun requestGetUser() {
        getUser.execute(
            onSuccess = { userEntity -> successUserLive.value = userEntity },
            onError = { throwable -> throwableLive.value = throwable }
        )
    }
}