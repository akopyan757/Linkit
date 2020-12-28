package com.akopyan757.linkit.viewmodel

import android.webkit.URLUtil
import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel : BaseViewModel() {

    @get:Bindable
    var displayName: String by DelegatedBindable("", BR.displayName)

    @get:Bindable
    var email: String by DelegatedBindable("", BR.email)

    @get:Bindable
    var isVerifyEmail: Boolean by DelegatedBindable(false, BR.verifyEmail)

    @get:Bindable
    var profileIconUrl: String? by DelegatedBindable(null, BR.profileIconUrl, BR.profileIconVisible)

    @get:Bindable
    val profileIconVisible: Boolean
        get() = URLUtil.isHttpsUrl(profileIconUrl) || URLUtil.isHttpUrl(profileIconUrl)

    @get:Bindable
    var profileIconDefaultRes: Int = R.drawable.ic_user

    fun setUser(firebaseUser: FirebaseUser) {
        displayName = firebaseUser.displayName ?: Config.EMPTY
        email = firebaseUser.email ?: Config.EMPTY
        profileIconUrl = firebaseUser.photoUrl?.toString()
    }
}