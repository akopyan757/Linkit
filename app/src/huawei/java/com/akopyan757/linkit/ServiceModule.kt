package com.akopyan757.linkit

import android.app.Activity
import com.akopyan757.linkit.network.CustomTokenApi
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceModule {


    val module = module {
        single<IAuthorizationService> { (activity: Activity) -> HMSAuthorizationService(activity)
        }

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