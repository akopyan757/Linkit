package com.akopyan757.linkit_domain.usecase

abstract class UseCase<T, P: UseCase.Params> {

    protected lateinit var parameters: P

    open fun execute(params: P): T {
        parameters = params
        return launch()
    }

    protected abstract fun launch(): T

    open class Params

    protected fun defaultOnNext(): () -> Unit = {}
    protected fun defaultOnError(): (Throwable) -> Unit = {}
    protected fun <V> defaultOnNextWithParams(): (V) -> Unit = {}
}