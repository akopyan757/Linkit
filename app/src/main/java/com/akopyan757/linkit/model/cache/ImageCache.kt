package com.akopyan757.linkit.model.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.view.scope.mainScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ImageCache: KoinComponent {

    private val imageDir: File by mainInject()

    fun saveImages(data: UrlLinkData) {
        val logoUrl = data.logoUrl
        val imageUrl = data.photoUrl

        if (logoUrl != null || imageUrl != null) {
            if (!imageDir.exists()) imageDir.mkdir()
        }

        if (logoUrl != null) {
            val bitmap = loadBitmap(logoUrl)
            val name = LOGO_PREFIX.format(data.hostPatternId)
            if (bitmap != null) {
                saveBitmap(bitmap, name)
                Log.i(TAG, "LOGO SAVED: NAME = $name")
            }
        }

        if (imageUrl != null) {
            val bitmap = loadBitmap(imageUrl)
            val name = CONTENT_PREFIX.format(data.hostPatternId, data.specPatternId, data.url.hashCode())
            if (bitmap != null) {
                saveBitmap(bitmap, name)
                Log.i(TAG, "CONTENT IMAGE SAVED: NAME = $name")
            }
        }
    }

    fun getLogoName(data: UrlLinkData): String? {
        return LOGO_PREFIX.format(data.hostPatternId)
            .takeIf { name -> File(imageDir, name).exists() }
    }

    fun getContentName(data: UrlLinkData): String? {
        return CONTENT_PREFIX.format(data.hostPatternId, data.specPatternId, data.url.hashCode())
            .takeIf { name -> File(imageDir, name).exists() }
    }

    fun clear() {
        if (imageDir.exists()) imageDir.delete()
    }

    private fun loadBitmap(urlValue: String): Bitmap? = try {
        val url = URL(urlValue)
        val inputStream = url.openConnection().getInputStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (exception: IOException) {
        null
    }

    private fun saveBitmap(bitmap: Bitmap, name: String) {
        val path = File(imageDir, name)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
    }

    companion object {
        private const val TAG = "IMAGE_CACHE"

        private const val IMAGE_QUALITY = 100

        private const val CONTENT_PREFIX = "content_%d_%d_%d.png"
        private const val LOGO_PREFIX = "logo_%d.png"
    }
}