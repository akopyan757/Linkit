package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable

data class LinkObservable(
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String? = null,
    val photoFileName: String? = null
): DiffItemObservable {
    override fun id() = url

    val descriptionVisible: Boolean
        get() = description.isNotEmpty()
}