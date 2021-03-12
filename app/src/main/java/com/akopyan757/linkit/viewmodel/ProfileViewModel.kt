package com.akopyan757.linkit.viewmodel

import android.webkit.URLUtil
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.FirebaseUserExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileViewModel : BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    @get:Bindable
    var displayName: String by DB("", BR.displayName)

    @get:Bindable
    var email: String by DB("", BR.email)

    @get:Bindable
    var profileIconUrl: String? by DB(null, BR.profileIconUrl, BR.profileIconVisible)

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

    fun getUserResponseLive() = requestConvert(
        request = authRepository.getUser(),
        onSuccess = { firebaseUser -> updateUiFromFirebaseUser(firebaseUser) }
    )

    fun getVerifyLiveResponse() = requestConvert(
        request = authRepository.emailVerification(),
        onSuccess = { verifyButtonEnabled = false }
    )

    fun getSignOutResponseLive() = requestConvert(
        request = authRepository.signOut(),
        onSuccess = {}
    )

    fun getEmailVerifyState(): LiveData<Boolean> = isVerifyEmailState

    private fun updateUiFromFirebaseUser(firebaseUser: FirebaseUser) {
        displayName = firebaseUser.displayName ?: Config.EMPTY
        email = firebaseUser.email ?: Config.EMPTY
        profileIconUrl = firebaseUser.photoUrl.toString()
        isVerifyEmailState.value = firebaseUser.isEmailVerified
        verifyButtonEnabled = firebaseUser.isEmailVerified.not()
        setPasswordButtonVisible = AuthWrapper.existsEmailProvider(firebaseUser).not()
        changePasswordButtonVisible = AuthWrapper.existsEmailProvider(firebaseUser)
        linkButtonVisible = FirebaseUserExtension.existServiceProvider(firebaseUser).not()
    }
}