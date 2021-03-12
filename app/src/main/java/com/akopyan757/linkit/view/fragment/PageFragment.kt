package com.akopyan757.linkit.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentPageBinding
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.viewmodel.PageViewModel
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PageFragment: BaseFragment<FragmentPageBinding, PageViewModel>(), LinkAdapterListener {

    override val mViewModel: PageViewModel by viewModel {
        val folderObservable = getFolderObservableFromArguments()
        parametersOf(folderObservable?.id)
    }

    private lateinit var recyclerLinksAdapter: LinkUrlAdapter
    private lateinit var recyclerLayoutManager: RecyclerView.LayoutManager

    override fun getVariableId() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_page

    override fun onSetupView(bundle: Bundle?) {
        updateRecyclerLinks()
        mViewModel.bindUrlList()
        mViewModel.getUrlLiveList().observeList(recyclerLinksAdapter)
        mViewModel.requestDeleteUrls().observeSuccessResponse {}
    }

    override fun onShareListener(link: LinkObservable) {
        if (mViewModel.isEditMode().not()) {
            openLinkSharing(link)
        }
    }

    override fun onItemListener(link: LinkObservable) {
        if (mViewModel.isEditMode()) {
            mViewModel.onLinkItemSelected(link)
        } else {
            openPreviewScreen(link)
        }
    }

    override fun onItemLongClickListener(link: LinkObservable) {
        mViewModel.onLinkItemSelected(link)
    }

    override fun onAdClosed(adObservable: AdObservable) {
        mViewModel.onAdClosed(adObservable)
    }

    private fun updateRecyclerLinks() {
        recyclerLinksAdapter = LinkUrlAdapter(this)
        recyclerLayoutManager = getLinksListLayoutManager()
        mBinding.rvLinks.adapter = recyclerLinksAdapter
        mBinding.rvLinks.layoutManager = getLinksListLayoutManager()
    }

    private fun getLinksListLayoutManager(): RecyclerView.LayoutManager {
        return if (isLandscapeOrientation())
            GridLayoutManager(requireContext(), 2)
        else
            LinearLayoutManagerWrapper(requireContext())
    }

    private fun isLandscapeOrientation(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun openPreviewScreen(observable: LinkObservable) {
        val bundle = bundleOf(PreviewUrlFragment.PREVIEW_URL to observable)
        findNavController().navigate(R.id.action_mainFragment_to_preview, bundle)
    }

    private fun openLinkSharing(linkObservable: LinkObservable) {
        startActivity(AndroidUtils.createShareIntent(linkObservable.url, linkObservable.title))
    }

    private fun getFolderObservableFromArguments(): FolderObservable? {
        return arguments?.getSerializable(TAG_FOLDER) as? FolderObservable
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