package com.akopyan757.base.viewmodel

interface DiffItemObservable {
    fun id(): Any? = hashCode()
}