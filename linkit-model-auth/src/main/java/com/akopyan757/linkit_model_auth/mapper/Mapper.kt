package com.akopyan757.linkit_model_auth.mapper

interface Mapper<T, P> {
    fun firstToSecond(data: T): P
}