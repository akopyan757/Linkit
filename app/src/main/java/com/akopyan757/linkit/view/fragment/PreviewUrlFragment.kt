package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentPreviewUrlBinding
import com.akopyan757.linkit.viewmodel.PreviewUrlViewModel
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf


class PreviewUrlFragment: BaseFragment<FragmentPreviewUrlBinding, PreviewUrlViewModel>(), KoinComponent {

    override val mViewModel: PreviewUrlViewModel by viewModel {
        parametersOf(getLinkObservableFromArgumentsOrNull())
    }

    override fun getLayoutId() = R.layout.fragment_preview_url
    override fun getVariableId() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPreviewObservableEmpty()) {
            findNavController().popBackStack()
        }
    }

    override fun onSetupView(binding: FragmentPreviewUrlBinding, bundle: Bundle?) = with(binding) {

        toolbarPreviewPage.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        wvPreviewPage.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                AndroidUtils.takeScreenshotFromWebView(view)
                moveScreenshotToCache()
            }
        }
    }

    fun moveScreenshotToCache() {
        mViewModel.requestMoveScreenshotToCache().observeSuccessResponse {
            showToast("Screenshot done")
        }
    }

    private fun getLinkObservableFromArgumentsOrNull(): LinkObservable? {
        return arguments?.getSerializable(PREVIEW_URL) as? LinkObservable
    }

    private fun isPreviewObservableEmpty(): Boolean {
        return arguments?.getSerializable(PREVIEW_URL) == null
    }

    companion object {
        const val PREVIEW_URL = "PREVIEW_URL"
    }
}