package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkObservable(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String?
): DiffItemObservable, Serializable {

    val photoVisible = photoUrl != null

    override fun id() = id

    companion object {
        fun from(data: UrlLinkEntity): LinkObservable {
            return LinkObservable(data.id, data.url, data.title, data.description, data.photoUrl)
        }
    }
}