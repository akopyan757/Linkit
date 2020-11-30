package com.akopyan757.linkit.view.adapter

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.akopyan757.linkit.common.image.getBitmapFromURL
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DatabindingAdapter {

    @JvmStatic
    @BindingAdapter("app:visibility")
    fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:url")
    fun ImageView.setUrl(url: String?) {
        Log.i("app:url", url ?: "")
        url?.takeUnless { it.isEmpty() } ?: return


        CoroutineScope(Dispatchers.Main).launch {
            val bitmapFromURL = try {
                getBitmapFromURL(url)
            } catch (e: IOException) {
                Log.e("TAG", "ERROR IO", e)
                null
            }

            setImageBitmap(bitmapFromURL)
        }
    }

    @JvmStatic
    @BindingAdapter("app:uri")
    fun ImageView.setUri(uri: Uri) {
        Picasso.get().load(uri).into(this)
    }

    @JvmStatic
    @BindingAdapter("app:error")
    fun TextInputLayout.setErrorMessage(message: String?) {
        error = message
    }
}