package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.ServiceConfig
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.model.service.network.CustomTokenApi
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    val module = module {

        single { AuthWrapper(androidContext()) }

        single {
            Retrofit.Builder()
                .baseUrl(ServiceConfig.FUNCTION_BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
        }

        single {
            get<Retrofit>().create(CustomTokenApi::class.java)
        }
    }
}