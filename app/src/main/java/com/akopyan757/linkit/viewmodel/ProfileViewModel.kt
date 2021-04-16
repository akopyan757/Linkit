package com.akopyan757.linkit.viewmodel

import android.webkit.URLUtil
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit_domain.usecase.auth.EmailVerificationUseCase
import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.akopyan757.linkit_domain_android_ext.usecase.SignOutUseCase
import org.koin.core.KoinComponent

class ProfileViewModel : BaseViewModel(), KoinComponent {

    private val getUser: GetUserUseCase by injectUseCase()
    private val emailVerification: EmailVerificationUseCase by injectUseCase()
    private val signOut: SignOutUseCase by injectUseCase()

    @get:Bindable var displayName: String by DB("", BR.displayName)
    @get:Bindable var email: String by DB("", BR.email)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl, BR.profileIconVisible)

    @get:Bindable
    val profileIconVisible: Boolean
        get() = URLUtil.isHttpsUrl(profileIconUrl) || URLUtil.isHttpUrl(profileIconUrl)

    @get:Bindable
    var profileIconDefaultRes: Int = R.drawable.ic_user

    @get:Bindable
    var verifyButtonEnabled: Boolean by DB(true, BR.verifyButtonEnabled)

    @get:Bindable
    var changePasswordButtonVisible: Boolean by DB(false, BR.changePasswordButtonVisible)

    @get:Bindable
    var setPasswordButtonVisible: Boolean by DB(false, BR.setPasswordButtonVisible)

    @get:Bindable
    var linkButtonVisible: Boolean by DB(false, BR.linkButtonVisible)

    private val isVerifyEmailState = MutableLiveData<Boolean>()
    private val verifyEmailToast = MutableLiveData<String>()

    fun getEmailVerifyState(): LiveData<Boolean> = isVerifyEmailState
    fun showSuccessVerifyEmailToast(): LiveData<String> = verifyEmailToast

    fun getUserRequest() {
        getUser.execute(
            onSuccess = { userEntity ->
                displayName = userEntity.displayName ?: Config.EMPTY
                email = userEntity.email
                profileIconUrl = userEntity.photoUrl
                isVerifyEmailState.value = userEntity.emailVerified
                verifyButtonEnabled = userEntity.emailVerified.not()
                setPasswordButtonVisible = userEntity.hasEmailProvider
                changePasswordButtonVisible = userEntity.hasEmailProvider
                linkButtonVisible = userEntity.hasEmailProvider.not()
            },
            onError = {
                emitAction(ACTION_DISMISS)
            }
        )
    }

    fun verifyEmail() {
        emailVerification.execute({
            verifyButtonEnabled = false
        })
    }

    fun signOut() {
        signOut.execute(onSuccess = {
            emitAction(ACTION_SHOW_AUTH)
        })
    }

    companion object {
        const val ACTION_DISMISS = 142_1
        const val ACTION_SHOW_AUTH = 142_2
    }
}