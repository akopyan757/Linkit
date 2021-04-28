package com.akopyan757.linkit.viewmodel.observable

import android.util.Log
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
            Log.i("LinkLargeObservable", "data: id=${data.id}; player=$isPlayer")
            return LinkLargeObservable(
                data.id, data.url, data.title, data.description, data.site ?: "",
                data.photoUrl, isPlayer
            ).apply {
                collapsed = data.collapsed
            }
        }
    }
}