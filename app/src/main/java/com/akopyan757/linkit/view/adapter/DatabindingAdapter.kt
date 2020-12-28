package com.akopyan757.linkit.view.adapter

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.image.getBitmapFromURL
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


object DatabindingAdapter {

    @JvmStatic
    @BindingAdapter("app:visibility")
    fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:photoUrl", "app:photoUrlDefaultRes")
    fun ImageView.setUrl(url: String?, @DrawableRes drawableRes: Int) {
        Picasso.get()
            .load(url)
            .error(drawableRes)
            .placeholder(drawableRes)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("app:uri")
    fun ImageView.setUri(uri: Uri?) {
        val radius = context.resources.getDimensionPixelOffset(R.dimen.tabPaddingHorizontally)
        val size = context.resources.getDimensionPixelOffset(R.dimen.linkPictureWidth)
        Picasso.get().load(uri).centerCrop().resize(size, size)
                .transform(RoundedCornersTransformation(radius, 0)).into(this)
    }

    @JvmStatic
    @BindingAdapter("app:error")
    fun TextInputLayout.setErrorMessage(message: String?) {
        error = message
    }

    @JvmStatic
    @BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
    fun bindSpinnerData(
        pAppCompatSpinner: Spinner,
        newSelectedValue: String,
        newTextAttrChanged: InverseBindingListener
    ) {
        pAppCompatSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                newTextAttrChanged.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val pos = (pAppCompatSpinner.adapter as? ArrayAdapter<String>)?.getPosition(newSelectedValue)
        if (pos != null) pAppCompatSpinner.setSelection(pos, true)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    fun captureSelectedValue(spinner: Spinner): String {
        return spinner.selectedItem as String
    }
}