package com.akopyan757.linkit.view.adapter

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.akopyan757.linkit.common.image.getBitmapFromURL
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
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