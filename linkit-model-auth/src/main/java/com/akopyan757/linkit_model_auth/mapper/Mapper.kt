package com.akopyan757.linkit_model_auth.mapper

interface MapperDirect<T, P> {
    fun firstToSecond(data: T): P
}