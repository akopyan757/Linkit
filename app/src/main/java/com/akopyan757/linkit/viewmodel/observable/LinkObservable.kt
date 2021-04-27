package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkObservable(
    override val id: String,
    override val url: String,
    override val title: String,
    val description: String,
    val site: String,
    val photoUrl: String?
): Serializable, BaseLinkObservable {

    val photoVisible = photoUrl != null

    override var checked: Boolean = false

    override fun resetCheck() {
        checked = false
    }

    override fun toggleCheck() {
        checked = checked.not()
    }

    companion object {
        fun from(data: UrlLinkEntity): LinkObservable {
            return LinkObservable(data.id, data.url, data.title, data.description, data.url, data.photoUrl)
        }
    }
}