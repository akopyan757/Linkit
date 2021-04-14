package com.akopyan757.linkit

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.akopyan757.linkit.common.di.*
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            // declare modules
            modules(
                listOf(
                    ContextModule.it, DatabaseModule.module, ModelModule.module,
                    ViewModelModule.module, UseCaseModule.module, ServiceModule.module,
                    NetworkModule.module
                )
            )

        }

        BannerViewExtension.init(this)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP", "DefaultException handler: $thread", throwable)
        }

    }
}