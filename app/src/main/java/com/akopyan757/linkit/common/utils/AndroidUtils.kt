package com.akopyan757.linkit.common.utils

import android.content.Intent
import android.net.Uri
import com.akopyan757.linkit.common.Config

object AndroidUtils {

    fun createShareIntent(
            linkUrl: String,
            linkTitle: String,
            linkPhotoUrl: String?
    ): Intent = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = Config.URL_TYPE
        putExtra(Intent.EXTRA_TEXT, linkUrl)
        putExtra(Intent.EXTRA_SUBJECT, linkUrl);
        putExtra(Intent.EXTRA_TITLE, linkTitle)
        if (linkPhotoUrl != null) data = Uri.parse(linkPhotoUrl)
    }, null)
}