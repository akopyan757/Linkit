package com.akopyan757.linkit.viewmodel.observable

import androidx.annotation.DimenRes
import com.akopyan757.linkit.R
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkObservable(
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
    val urlMaxLines: Int get() = 3

    @DimenRes val smallPictureSizeRes = R.dimen.linkPictureSmallWidth

    override var checked: Boolean = false

    override fun resetCheck() {
        checked = false
    }

    override fun toggleCheck() {
        checked = checked.not()
    }

    companion object {
        fun from(data: UrlLinkEntity): LinkObservable {
            val isPlayer = data.type == UrlLinkEntity.Type.PLAYER
            val site = data.site ?: data.url
            val app = data.app?.let { app -> LinkAppObservable.from(app) }
            return LinkObservable(
                data.id, data.url, data.title, data.description, site, data.photoUrl, isPlayer, app
            )
        }
    }
}