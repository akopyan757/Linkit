package com.akopyan757.linkit.model.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.UrlLinkData

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ImageCache: KoinComponent {

    private val cacheDir: File by inject(named(Config.CACHE_DIR))
    private val imageDir: File by inject(named(Config.CACHE_IMAGE_DIR))

    fun saveImages(data: UrlLinkData) {
        val logoUrl = data.logoUrl
        val imageUrl = data.photoUrl

        if (logoUrl != null || imageUrl != null) {
            if (!imageDir.exists()) imageDir.mkdir()
        }

        if (logoUrl != null) {
            val bitmap = loadBitmap(logoUrl)
            val name = LOGO_PREFIX.format(data.id)
            if (bitmap != null) {
                saveBitmap(bitmap, name)
                Log.i(TAG, "LOGO SAVED: NAME = $name")
            }
        }

        if (imageUrl != null) {
            val bitmap = loadBitmap(imageUrl)
            val name = CONTENT_PREFIX.format(data.id, data.url.hashCode())
            if (bitmap != null) {
                saveBitmap(bitmap, name)
                Log.i(TAG, "CONTENT IMAGE SAVED: NAME = $name")
            }
        }
    }

    fun moveScreenshot(linkId: Long) {
        val screenshotFile = File(cacheDir, Config.SCREENSHOT_FILENAME)
        val imageFile = File(imageDir, SCREENSHOT_PREFIX.format(linkId))
        screenshotFile.copyTo(imageFile, true)
        screenshotFile.delete()
    }

    fun getLogoName(data: UrlLinkData): String? {
        return LOGO_PREFIX.format(data.id)
            .takeIf { name -> File(imageDir, name).exists() }
    }

    fun getContentName(data: UrlLinkData): String? {
        return CONTENT_PREFIX.format(data.id, data.url.hashCode())
            .takeIf { name -> File(imageDir, name).exists() }
    }

    fun getScreenshotName(id: Long): String? {
        return SCREENSHOT_PREFIX.format(id).takeIf { name -> File(imageDir, name).exists() }
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

        private const val CONTENT_PREFIX = "content_%d_%d.png"
        private const val SCREENSHOT_PREFIX = "screenshot_%d.png"
        private const val LOGO_PREFIX = "logo_%d.png"
    }
}