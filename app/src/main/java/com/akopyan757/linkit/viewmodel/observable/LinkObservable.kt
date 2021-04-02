package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit.model.entity.UrlLinkData
import java.io.Serializable

data class LinkObservable(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String?,
    val shareVisible: Boolean = true
): DiffItemObservable, Serializable {

    val photoVisible = photoUrl != null

    override fun id() = id

    companion object {
        fun from(data: UrlLinkData): LinkObservable {
            return LinkObservable(data.id, data.url, data.title, data.description, data.photoUrl)
        }
    }
}