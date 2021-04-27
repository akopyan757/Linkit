package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkLargeObservable(
    override val id: String,
    override val url: String,
    override val title: String,
    val description: String,
    val site: String,
    val photoUrl: String?,
    val isPlayer: Boolean
): Serializable, BaseLinkObservable {

    val photoVisible = photoUrl != null
    val descriptionVisible: Boolean = description.isNotEmpty()
    override var checked: Boolean = false

    override fun resetCheck() {
        checked = false
    }

    override fun toggleCheck() {
        checked = checked.not()
    }

    companion object {
        fun from(data: UrlLinkEntity): LinkLargeObservable {
            val isPlayer = data.type == UrlLinkEntity.Type.PLAYER
            return LinkLargeObservable(
                data.id, data.url, data.title, data.description, data.site ?: "",
                data.photoUrl, isPlayer
            )
        }
    }
}