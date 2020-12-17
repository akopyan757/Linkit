package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable

data class LinkObservable(
    val id: Long,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String? = null,
    val photoFileName: String? = null,
    var selected: Boolean = false
): DiffItemObservable {
    override fun id() = id

    val descriptionVisible: Boolean
        get() = description.isNotEmpty()
}