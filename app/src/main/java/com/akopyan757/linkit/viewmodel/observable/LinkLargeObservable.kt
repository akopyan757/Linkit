package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import java.io.Serializable

data class LinkLargeObservable(
    override val id: String,
    override val url: String,
    override val title: String,
    val description: String,
    val photoUrl: String?
): DiffItemObservable, Serializable, BaseLinkObservable {

    val photoVisible = photoUrl != null
    val descriptionVisible: Boolean = description.isNullOrEmpty().not()

    override fun id() = id

    companion object {
        fun from(data: UrlLinkEntity): LinkLargeObservable {
            return LinkLargeObservable(
                data.id, data.site ?: "", data.title, data.description, data.photoUrl
            )
        }
    }
}