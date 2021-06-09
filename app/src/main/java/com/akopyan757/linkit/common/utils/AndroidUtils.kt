package com.akopyan757.linkit.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.akopyan757.linkit.common.Config


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

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
            activity.currentFocus?.windowToken, InputMethodManager.SHOW_FORCED, 0
        )
    }
}