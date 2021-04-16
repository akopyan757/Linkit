package com.akopyan757.linkit.viewmodel

import android.content.Intent
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceIntentSignInUseCase
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceUserUserCase
import org.koin.core.KoinComponent

class AuthStartViewModel: BaseViewModel(), KoinComponent {

    private val getServiceUser: GetServiceUserUserCase by injectUseCase()
    private val getIntentSignIn: GetServiceIntentSignInUseCase by injectUseCase()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)

    private val signInIntentLive = MutableLiveData<Intent>()
    private val showMainScreenByUser = MutableLiveData<String>()

    fun getSignInIntentLive(): LiveData<Intent> {
        return signInIntentLive
    }

    fun showMainScreenWithUserLive(): LiveData<String> {
        return showMainScreenByUser
    }

    fun getUserFromService(data: Intent?) {
        isProgress = true
        getServiceUser.execute(GetServiceUserUserCase.Params(data),
            onSuccess = { userEntity ->
                isProgress = false
                showMainScreenByUser.value = userEntity.uid
            }, onError = {
                isProgress = false
            })
    }

    fun signInService() {
        getIntentSignIn.execute(onSuccess = { data ->
            signInIntentLive.value = data
        })
    }
}