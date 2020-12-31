package com.akopyan757.linkit.model.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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

    fun saveScreenshot(webView: WebView, linkObservable: LinkObservable) {
        val name = SCREENSHOT_PREFIX.format(linkObservable.id)
        if (File(imageDir, name).exists()) return

        val width = webView.measuredWidth
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        webView.measure(widthSpec, heightSpec)
        val bm = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val size = bm?.width?.toFloat() ?: 0F
        val bigCanvas = Canvas(bm)
        bigCanvas.drawBitmap(bm, 0F, size, Paint())
        webView.draw(bigCanvas)
        if (bm != null) {
            var fOut: OutputStream? = null
            try {
                val file = File(imageDir, name)
                fOut = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                bm.recycle()
            } catch (e: Exception) {
               Log.e(TAG, "ERROR", e)
            } finally {
                fOut?.flush()
                fOut?.close()
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

    fun getScreenshotName(id: Long): String? {
        return SCREENSHOT_PREFIX.format(id).takeIf { name -> File(imageDir, name).exists() }
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
        private const val SCREENSHOT_PREFIX = "screenshot_%d.png"
        private const val LOGO_PREFIX = "logo_%d.png"
    }
}