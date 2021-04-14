package com.akopyan757.linkit_model.mapper

interface Mapper<T, P> {
    fun firstToSecond(data: T): P
    fun secondToFirst(data: P): T
}