package com.akopyan757.linkit.common.image

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

suspend fun getBitmapFromURL(src: String) = withContext(Dispatchers.IO) {
    val connection = URL(src).openConnection().apply {
        doInput = true
        connect()
    }
    val input = connection.getInputStream()
    val options = BitmapFactory.Options()

    BitmapFactory.decodeStream(input, null, options)
}
