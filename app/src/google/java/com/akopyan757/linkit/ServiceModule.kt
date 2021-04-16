package com.akopyan757.linkit

import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import com.akopyan757.linkit_model_auth.AuthServiceDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ServiceModule {

    private const val MAPPER_USER = "user"

    val module = module {
        single<IAuthIntentDataSource> {
            val context = androidContext()
            val defaultWebClientId = context.resources.getString(R.string.default_web_client_id)
            AuthServiceDataSource(context, defaultWebClientId, get(named(MAPPER_USER)))
        }
    }
}