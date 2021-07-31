package com.cheesecake.linkit.ui.auth.start

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceIntentSignInUseCase
import com.akopyan757.linkit_domain_android_ext.usecase.GetServiceUserUserCase
import com.cheesecake.linkit.compose.BaseViewModel

class AuthStartViewModel : BaseViewModel() {

    private val getServiceUser: GetServiceUserUserCase by injectUseCase()
    private val getIntentSignIn: GetServiceIntentSignInUseCase by injectUseCase()

    val progressVisibility = MutableLiveData(false)



    fun getUserFromService(data: Intent?, onMainStart: (uid: String) -> Unit) {
        progressVisibility.value = true
        getServiceUser.execute(GetServiceUserUserCase.Params(data),
            onSuccess = { userEntity ->
                progressVisibility.value = false
                onMainStart.invoke(userEntity.uid)
            }, onError = {
                progressVisibility.value = false
            })
    }

    fun signInService(onSignIn: (Intent) -> Unit) {
        getIntentSignIn.execute(onSuccess = onSignIn)
    }
}