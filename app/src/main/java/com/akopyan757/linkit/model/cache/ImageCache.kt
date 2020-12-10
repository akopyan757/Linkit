package com.akopyan757.linkit.model.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.UrlLinkData
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class ImageCache: KoinComponent {

    private val cacheDir: File by inject()
    private val imageDir: File by lazy { File(cacheDir, Config.CACHE_IMAGES_FOLDER) }

    fun saveImages(data: UrlLinkData) {
        val logoUrl = data.logoUrl
        val imageUrl = data.photoUrl

        if (logoUrl != null || imageUrl != null) {
            if (!imageDir.exists()) imageDir.mkdir()
        }

        if (logoUrl != null) {
            val bitmap = loadBitmap(logoUrl)?.getResizedBitmap(MAX_SIZE)
            val name = LOGO_PREFIX.format(data.hostPatternId)
            if (bitmap != null) {
                saveBitmap(bitmap, name)
                Log.i(TAG, "LOGO SAVED: NAME = $name")
            }
        }

        if (imageUrl != null) {
            val bitmap = loadBitmap(imageUrl)?.getResizedBitmap(MAX_SIZE)
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

    private fun loadBitmap(urlValue: String): Bitmap? {
        val url = URL(urlValue)
        val inputStream = url.openConnection().getInputStream()
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun Bitmap.getResizedBitmap(maxSize: Int): Bitmap {
        var widthValue = width
        var heightValue = height
        val bitmapRatio = widthValue.toFloat() / heightValue.toFloat()
        if (bitmapRatio > 1) {
            widthValue = maxSize
            heightValue = (width / bitmapRatio).toInt()
        } else {
            heightValue = maxSize
            widthValue = (heightValue * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(this, widthValue, heightValue, true)
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

        private const val MAX_SIZE = 500
        private const val IMAGE_QUALITY = 100

        private const val CONTENT_PREFIX = "content_%d_%d_%d.png"
        private const val LOGO_PREFIX = "logo_%d.png"
    }
}