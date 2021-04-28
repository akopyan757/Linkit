package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.linkit_domain.entity.UrlLinkAppEntity

data class LinkAppObservable(
    val appId: String,
    val appName: String,
    val url: String
) {
    companion object {
        fun from(data: UrlLinkAppEntity): LinkAppObservable? {
            val appId = data.appId ?: return null
            val url = data.url ?: return null
            val appName = data.appName ?: ""
            return LinkAppObservable(appId, appName, url)
        }
    }
}