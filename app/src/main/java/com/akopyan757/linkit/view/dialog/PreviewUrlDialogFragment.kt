package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogPreviewUrlBinding
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.PreviewUrlViewModel
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class PreviewUrlDialogFragment: BaseDialogFragment<DialogPreviewUrlBinding, PreviewUrlViewModel>(), KoinComponent {

    override val mViewModel: PreviewUrlViewModel by viewModel()

    private val imageCache: ImageCache by mainInject()

    override fun getLayoutId() = R.layout.dialog_preview_url
    override fun getVariableId() = BR.viewModel

    override fun onSetupView(binding: DialogPreviewUrlBinding, bundle: Bundle?): Unit = with(binding) {

        val observable = arguments?.getSerializable(PREVIEW_URL) as? LinkObservable ?: return@with

        wvPreviewPage.apply {
            settings.builtInZoomControls = true
            loadUrl(observable.url)
            webViewClient = object: WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    imageCache.saveScreenshot(view, observable)
                }
            }
        }
    }

    companion object {
        const val PREVIEW_URL = "PREVIEW_URL"
    }
}