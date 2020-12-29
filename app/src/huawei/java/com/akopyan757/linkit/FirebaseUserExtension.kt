package com.akopyan757.linkit

import com.google.firebase.auth.FirebaseUser

object FirebaseUserExtension {

    fun existServiceProvider(firebaseUser: FirebaseUser): Boolean {
        return firebaseUser.uid.startsWith(HUAWEI_PREFIX)
    }

    private const val HUAWEI_PREFIX = "huawei_"
}