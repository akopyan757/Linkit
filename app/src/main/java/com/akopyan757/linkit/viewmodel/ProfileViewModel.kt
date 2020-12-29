package com.akopyan757.linkit.viewmodel

import android.webkit.URLUtil
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.FirebaseUserExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.model.repository.AuthRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileViewModel : BaseViewModel(), KoinComponent {

    /** Databinding properties */
    @get:Bindable
    var displayName: String by DelegatedBindable("", BR.displayName)

    @get:Bindable
    var email: String by DelegatedBindable("", BR.email)

    @get:Bindable
    var profileIconUrl: String? by DelegatedBindable(null, BR.profileIconUrl, BR.profileIconVisible)

    @get:Bindable
    val profileIconVisible: Boolean
        get() = URLUtil.isHttpsUrl(profileIconUrl) || URLUtil.isHttpUrl(profileIconUrl)

    @get:Bindable
    var profileIconDefaultRes: Int = R.drawable.ic_user

    @get:Bindable
    var verifyButtonEnabled: Boolean by DelegatedBindable(true, BR.verifyButtonEnabled)

    @get:Bindable
    var changePasswordButtonVisible: Boolean by DelegatedBindable(false, BR.changePasswordButtonVisible)

    @get:Bindable
    var setPasswordButtonVisible: Boolean by DelegatedBindable(false, BR.setPasswordButtonVisible)

    @get:Bindable
    var linkButtonVisible: Boolean by DelegatedBindable(false, BR.linkButtonVisible)

    /** View live properties */
    private val isVerifyEmail = MutableLiveData<Boolean>()

    /** Models */
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestSignOut = MutableLiveData<Unit>()
    private val requestVerify = MutableLiveData<Unit>()

    /** DataBinding methods */
    fun onSignOutButtonClicked() {
        requestSignOut.value = Unit
    }

    fun onVerifyButtonClicked() {
        requestVerify.value = Unit
    }

    /** Responses */
    fun getUserResponseLive() = requestConvertSimple(
        method = { authRepository.getUser() },
        onSuccess = { firebaseUser ->
            displayName = firebaseUser.displayName ?: Config.EMPTY
            email = firebaseUser.email ?: Config.EMPTY
            profileIconUrl = firebaseUser.photoUrl?.toString()
            isVerifyEmail.value = firebaseUser.isEmailVerified
            verifyButtonEnabled = firebaseUser.isEmailVerified.not()
            setPasswordButtonVisible = AuthWrapper.existsEmailProvider(firebaseUser).not()
            changePasswordButtonVisible = AuthWrapper.existsEmailProvider(firebaseUser)
            linkButtonVisible = FirebaseUserExtension.existServiceProvider(firebaseUser).not()
        }
    )

    fun getVerifyResponseLive() = requestVerify.switchMap {
        requestConvertSimple<String>(
            method = { authRepository.emailVerification() },
            onSuccess = { verifyButtonEnabled = false }
        )
    }

    fun getSignOutResponseLive() = requestSignOut.switchMap {
        requestConvertSimple<Unit>({ authRepository.signOut() })
    }

    fun getEmailVerifyState(): LiveData<Boolean> {
        return isVerifyEmail
    }

    companion object {
        private const val TAG = "PROFILE_VIEW_MODEL"

        private const val HUAWEI_PREFIX = "huawei_"
    }
}