package com.akopyan757.linkit.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentPageBinding
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.view.dialog.PreviewUrlDialogFragment
import com.akopyan757.linkit.viewmodel.PageViewModel
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PageFragment: BaseFragment<FragmentPageBinding, PageViewModel>(), LinkAdapterListener {

    override val mViewModel: PageViewModel by viewModel { parametersOf(observable.id) }

    private lateinit var observable: FolderObservable

    override fun getVariableId() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observable = arguments?.getSerializable(TAG_FOLDER) as? FolderObservable ?: return
    }

    private val mUrlAdapter: LinkUrlAdapter by lazy {
        LinkUrlAdapter(this)
    }

    override fun onSetupView(binding: FragmentPageBinding, bundle: Bundle?): Unit = with(binding) {

        val urlLayoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(requireContext(), 2)
            else -> LinearLayoutManagerWrapper(requireContext())
        }

        fragmentWebLinkList.apply {
            adapter = mUrlAdapter
            layoutManager = urlLayoutManager
        }
    }

    override fun onSetupViewModel(viewModel: PageViewModel): Unit = with(viewModel) {
        bindUrlList()
        getUrlLiveList().observeList(mUrlAdapter)
        getDeleteUrlsResponseLive().apply {
            successResponse {}
        }
    }

    override fun onShareListener(link: LinkObservable) {
        if (!mViewModel.getEditModeState()) {
            startActivity(AndroidUtils.createShareIntent(link.url, link.title))
        }
    }

    override fun onItemListener(link: LinkObservable) {
        if (mViewModel.getEditModeState()) {
            mViewModel.onItemSelected(link)
        } else {
            Log.i("PAGE_FRAGMENT", "onItemListener($link)")
            val bundle = bundleOf(PreviewUrlDialogFragment.PREVIEW_URL to link)
            findNavController().navigate(R.id.action_mainFragment_to_preview, bundle)
        }
    }

    override fun onItemLongClickListener(link: LinkObservable) {
        mViewModel.onItemSelected(link)
    }

    override fun onAdClosed(adObservable: AdObservable) {
        mViewModel.onAdClosed(adObservable)
    }

    companion object {
        private const val TAG_FOLDER = "FOLDER"

        fun newInstance(observable: FolderObservable) = PageFragment().also { fragment ->
            fragment.arguments = Bundle().apply {
                putSerializable(TAG_FOLDER, observable)
            }
        }
    }
}