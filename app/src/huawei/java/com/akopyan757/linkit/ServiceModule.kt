package com.akopyan757.linkit

import com.akopyan757.linkit.view.service.IAuthorizationService
import org.koin.dsl.module

object ServiceModule {

    val module = module {
        single<IAuthorizationService> { HmsAuthorizationService() }
    }
}