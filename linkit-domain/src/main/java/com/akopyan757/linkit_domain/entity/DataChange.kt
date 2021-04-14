package com.akopyan757.linkit_domain.entity

sealed class DataChange<T> {
    data class Initialized<T>(val data: List<T>): DataChange<T>()
    data class Added<T>(val data: T): DataChange<T>()
    data class Modified<T>(val data: T): DataChange<T>()
    data class Deleted<T>(val data: T): DataChange<T>()
}