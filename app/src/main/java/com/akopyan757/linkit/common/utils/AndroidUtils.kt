package com.akopyan757.linkit.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.akopyan757.linkit.BuildConfig
import com.akopyan757.linkit.common.Config
import java.io.File

object AndroidUtils {

    fun createShareIntent(
            linkUrl: String,
            linkTitle: String,
            imageUri: Uri?
    ): Intent = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = Config.URL_TYPE
        putExtra(Intent.EXTRA_TEXT, linkUrl)
        putExtra(Intent.EXTRA_SUBJECT, linkUrl)
        putExtra(Intent.EXTRA_TITLE, linkTitle)
        if (imageUri != null)
            putExtra(Intent.EXTRA_STREAM, imageUri)
    }, linkTitle)

    fun getUriFromCache(context: Context, name: String?): Uri? {
        if (name == null) return null
        val cacheDir = context.cacheDir ?: return null
        val file = File(cacheDir, Config.CACHE_IMAGES_FOLDER + "/" + name)
        val authority = BuildConfig.APPLICATION_ID + Config.PROVIDER_NAME
        return FileProvider.getUriForFile(context, authority, file)
    }
}