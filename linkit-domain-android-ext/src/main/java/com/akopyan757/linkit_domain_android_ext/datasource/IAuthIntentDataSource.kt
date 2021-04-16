package com.akopyan757.linkit_domain_android_ext.datasource

import android.content.Intent
import com.akopyan757.linkit_domain.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IAuthIntentDataSource {
    fun getServiceSignInIntent(): Single<Intent>
    fun getServiceUser(data: Intent?): Single<UserEntity>
    fun signOut(): Completable
}