package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable

interface BaseLinkObservable: DiffItemObservable {
    val id: String
    val url: String
    val title: String
    var checked: Boolean
    val app: LinkAppObservable?
    fun resetCheck()
    fun toggleCheck()
    override fun id() = id
}