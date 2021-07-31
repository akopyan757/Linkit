package com.cheesecake.linkit.compose.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.URLUtil


object ClipboardUtils {

    private const val EMPTY = ""

    fun getUrl(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = clipboard?.primaryClip ?: return null
        val count = clip.itemCount
        return  (0 until count).mapNotNull { index ->
            clip.getItemAt(index)?.text?.toString()
        }.firstOrNull { URLUtil.isValidUrl(it) }
    }

    fun clear(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val data = ClipData.newPlainText(EMPTY, EMPTY)
        clipboard?.setPrimaryClip(data)
    }
}