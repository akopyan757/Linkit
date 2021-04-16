package com.akopyan757.linkit

import android.content.Context
import android.content.SharedPreferences
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import com.akopyan757.linkit_model_auth.AuthServiceDataSource
import com.akopyan757.linkit_model_auth.cache.ITokenCache
import com.akopyan757.linkit_model_auth.cache.TokenCache
import com.akopyan757.linkit_model_auth.network.CustomTokenApi
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceModule {

    private const val KEY_SHARED = "SHARED_PREFS"
    private const val MAPPER_USER = "user"

    val module = module {

        single<SharedPreferences> {
            androidContext().getSharedPreferences(KEY_SHARED, Context.MODE_PRIVATE)
        }

        single<ITokenCache> { TokenCache(get()) }

        single {
            Retrofit.Builder()
                .baseUrl(ServiceConfig.FUNCTION_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
        }

        single {
            get<Retrofit>().create(CustomTokenApi::class.java)
        }

        single<IAuthIntentDataSource> {
            AuthServiceDataSource(androidContext(), get(), get(), get(named(MAPPER_USER)))
        }
    }
}