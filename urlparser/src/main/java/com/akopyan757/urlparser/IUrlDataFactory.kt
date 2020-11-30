package com.akopyan757.urlparser

interface IUrlDataFactory<T : UrlData> {
    fun createData(): T
}