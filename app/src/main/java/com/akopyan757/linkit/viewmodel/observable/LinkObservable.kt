package com.akopyan757.linkit.viewmodel.observable

import android.net.Uri
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit.model.entity.UrlLinkData
import java.io.Serializable

data class LinkObservable(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoFileName: String? = null,
    var selected: Boolean = false,
    var uri: Uri? = null,
    var photoVisible: Boolean = false
): DiffItemObservable, Serializable {
    override fun id() = id

    companion object {
        fun from(data: UrlLinkData): LinkObservable {
            return LinkObservable(data.id, data.url, data.title, data.description)
        }
    }
}