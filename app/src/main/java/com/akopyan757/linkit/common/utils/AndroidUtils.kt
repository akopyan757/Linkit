package com.akopyan757.linkit.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import androidx.core.content.FileProvider
import com.akopyan757.linkit.BuildConfig
import com.akopyan757.linkit.common.Config
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object AndroidUtils {

    fun createIntent(linkUrl: String) = Intent(Intent.ACTION_VIEW).apply {
        type = Config.URL_TYPE
        data = Uri.parse(linkUrl)
    }

    fun createShareIntent(
        linkUrl: String,
        linkTitle: String
    ): Intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = Config.URL_TYPE
        putExtra(Intent.EXTRA_TEXT, linkUrl)
        putExtra(Intent.EXTRA_TITLE, linkTitle)
        putExtra(Intent.EXTRA_SUBJECT, linkTitle)
    }, null)

    fun getUriFromCache(context: Context, name: String?): Uri? {
        if (name == null) return null
        val cacheDir = context.cacheDir ?: return null
        val file = File(cacheDir, Config.CACHE_IMAGES_FOLDER + "/" + name)
        val authority = BuildConfig.APPLICATION_ID + Config.PROVIDER_NAME
        return FileProvider.getUriForFile(context, authority, file)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun takeScreenshotFromWebView(webView: WebView) {
        val cacheDir = webView.context.cacheDir
        val imageFile = File(cacheDir, Config.SCREENSHOT_FILENAME)

        val width = webView.measuredWidth
        val bm = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val bigCanvas = Canvas(bm)
        bigCanvas.drawBitmap(bm, 0F, 0F, Paint())
        webView.draw(bigCanvas)

        var fOut: OutputStream? = null
        try {
            fOut = FileOutputStream(imageFile)
            bm.compress(Bitmap.CompressFormat.PNG, Config.SCREENSHOT_QUALITY, fOut)
            bm.recycle()
        } catch (e: Exception) {
            Log.e("takeScreenshotWeb", "ERROR", e)
        } finally {
            fOut?.flush()
            fOut?.close()
        }
    }
}