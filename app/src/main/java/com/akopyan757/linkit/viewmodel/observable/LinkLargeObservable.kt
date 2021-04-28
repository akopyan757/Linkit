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
    val isPlayer: Boolean,
    override val app: LinkAppObservable?
): Serializable, BaseLinkObservable {

    val photoVisible: Boolean get() = photoUrl.isNullOrEmpty().not()
    val descriptionVisible: Boolean get() =  description.isNotEmpty()
    val titleVisible: Boolean get() = title.isNotEmpty()
    override var checked: Boolean = false
    private var collapsed: Boolean = false

    override fun resetCheck() {
        checked = false
    }

    override fun toggleCheck() {
        checked = checked.not()
    }

    fun isCollapsed() = collapsed

    fun toggleCollapsed() {
        collapsed = collapsed.not()
    }

    companion object {
        fun from(data: UrlLinkEntity): LinkLargeObservable {
            val isPlayer = data.type == UrlLinkEntity.Type.PLAYER
            val site = data.site ?: data.url
            val app = data.app?.let { app -> LinkAppObservable.from(app) }
            return LinkLargeObservable(
                data.id, data.url, data.title, data.description, site, data.photoUrl, isPlayer, app
            ).apply {
                collapsed = data.collapsed
            }
        }
    }
}