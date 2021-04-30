package com.akopyan757.linkit.view.adapter

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.net.URI
import java.net.URL
import kotlin.math.abs


object DatabindingAdapter {

    @JvmStatic
    @BindingAdapter("android:onLongClick")
    fun View.setOnLongClickListener(onLongClick: () -> Unit) {
        setOnLongClickListener {
            onLongClick.invoke()
            return@setOnLongClickListener true
        }
    }

    @JvmStatic
    @BindingAdapter("app:visibility")
    fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(
        "app:photoUrl",
        "app:photoBaseUrl",
        "app:photoUrlDefaultRes",
        "app:squareSize",
        requireAll = false
    )
    fun ImageView.setUrl(
        photoUrl: String?,
        baseUrl: String?,
        @DrawableRes drawableRes: Int?,
        @DimenRes squareResSize: Int?,
    ) {
        val imageUrl = if (photoUrl != null) {
            if (URI(photoUrl).isAbsolute) photoUrl else {
                URL(baseUrl).let { it.protocol + "://" + it.host + photoUrl }
            }
        } else null
        var request = Picasso.get()
            .load(imageUrl)

        val size = (this.context.resources.displayMetrics.density * 50f).toInt()
        request = request.transform(object : Transformation {
            override fun transform(source: Bitmap?): Bitmap? {
                val bitmap = source ?: return null
                val aspectRatio = bitmap.height.toFloat() / source.width.toFloat()
                return if (abs(aspectRatio - 1.0) < 0.05) {
                    val result = Bitmap.createScaledBitmap(bitmap, size, size, false)
                    if (result != bitmap) bitmap.recycle()
                    result
                } else bitmap
            }

            override fun key(): String = "cropPosterTransformation$size"
        })
        if (drawableRes != null) {
            request = request
                .error(drawableRes)
                .placeholder(drawableRes)
        }
        request.into(this)
    }

    @JvmStatic
    @BindingAdapter("app:error")
    fun TextInputLayout.setErrorMessage(message: String?) {
        error = message
    }

    @JvmStatic
    @BindingAdapter("app:loadUrl")
    fun WebView.setPreviewUrl(url: String) {
        loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["app:selectedValue", "selectedValueAttrChanged"],
        requireAll = false
    )
    fun bindSpinnerData(
        pAppCompatSpinner: Spinner,
        newSelectedValue: String,
        newTextAttrChanged: InverseBindingListener
    ) {
        pAppCompatSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                newTextAttrChanged.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val adapter = pAppCompatSpinner.adapter as? ArrayAdapter<String>
        val pos = adapter?.getPosition(newSelectedValue)
        if (pos != null) pAppCompatSpinner.setSelection(pos, true)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "app:selectedValue", event = "selectedValueAttrChanged")
    fun captureSelectedValue(spinner: Spinner): String {
        return spinner.selectedItem as String
    }
}