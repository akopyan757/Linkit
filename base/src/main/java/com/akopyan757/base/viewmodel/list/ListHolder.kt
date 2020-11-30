package com.akopyan757.base.viewmodel.list

data class ListHolder<T>(
    val data: List<T>,
    val strategy: ListChangeStrategy
)