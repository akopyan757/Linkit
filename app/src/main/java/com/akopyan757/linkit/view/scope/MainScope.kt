package com.akopyan757.linkit.view.scope

import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.view.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

fun KoinComponent.mainScopeId(): String {
    return getKoin().getProperty(Config.KEY_USER_ID, Config.EMPTY)
}

fun KoinComponent.mainScope(): Scope {
    return getKoin().getOrCreateScope(mainScopeId(), named<MainActivity>())
}

inline fun <reified T> KoinComponent.mainInject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    return getKoin().getOrCreateScope(mainScopeId(), named<MainActivity>()).inject(qualifier, parameters)
}