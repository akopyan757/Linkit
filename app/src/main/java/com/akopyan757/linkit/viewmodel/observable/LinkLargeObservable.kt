package com.akopyan757.linkit.viewmodel.observable

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit.R
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
): DiffItemObservable, Serializable, BaseLinkObservable {

    val photoVisible = photoUrl != null
    val descriptionVisible: Boolean = description.isNullOrEmpty().not()
    @DimenRes val cornerRadius: Int = R.dimen.marginTertiary

    override fun id() = id

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