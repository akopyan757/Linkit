package com.akopyan757.linkit.viewmodel.observable

import androidx.annotation.DimenRes
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit.R
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkObservable(
    override val id: String,
    override val url: String,
    override val title: String,
    val description: String,
    val photoUrl: String?
): DiffItemObservable, Serializable, BaseLinkObservable {

    val photoVisible = photoUrl != null
    @DimenRes val cornerRadiusRes = R.dimen.marginSecondary

    override fun id() = id

    companion object {
        fun from(data: UrlLinkEntity): LinkObservable {
            return LinkObservable(data.id, data.url, data.title, data.description, data.photoUrl)
        }
    }
}