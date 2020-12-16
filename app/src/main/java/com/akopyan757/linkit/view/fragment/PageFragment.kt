package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
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
import com.akopyan757.linkit.viewmodel.listener.LinkClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PageFragment: BaseFragment<ViewDataBinding, PageViewModel>(), LinkClickListener {

    override val mViewModel: PageViewModel by viewModel { parametersOf(mObservable.id) }

    private lateinit var mObservable: FolderObservable

    override fun getVariableId() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mObservable = arguments?.getSerializable(TAG_FOLDER) as? FolderObservable ?: return
    }

    override fun getLayoutId() = R.layout.fragment_page

    private val mUrlAdapter: LinkUrlAdapter by lazy {

        when (mObservable.type) {
            1 -> LinkUrlAdapter(LinkUrlAdapter.Type.ITEM, this)
            else -> LinkUrlAdapter(LinkUrlAdapter.Type.BOX, this)
        }
    }

    private lateinit var mUrlLayoutManager: RecyclerView.LayoutManager

    override fun onSetupView(binding: ViewDataBinding, bundle: Bundle?) {
        val urlRecyclerView = when (binding) {
            is FragmentPageBinding -> binding.fragmentWebLinkList
            else -> null
        }

        val urlLayoutManager = when (mObservable.type) {
            1 -> LinearLayoutManagerWrapper(requireContext())
            else -> GridLayoutManager(requireContext(), 2)
        }

        urlRecyclerView?.apply {
            adapter = mUrlAdapter
            layoutManager = urlLayoutManager
        }
    }

    override fun onSetupViewModel(viewModel: PageViewModel) = with(viewModel) {
        getUrlLiveList().observeList(mUrlAdapter)
    }

    override fun onShareListener(link: LinkObservable) {
        startActivity(AndroidUtils.createShareIntent(link.url, link.title))
    }

    override fun onItemListener(link: LinkObservable) {
        startActivity(AndroidUtils.createIntent(link.url))
    }

    companion object {
        private const val TAG = "PAGE_FRAGMENT"
        private const val TAG_FOLDER = "FOLDER"

        fun newInstance(observable: FolderObservable) = PageFragment().also { fragment ->
            fragment.arguments = Bundle().apply {
                putSerializable(TAG_FOLDER, observable)
            }
        }
    }
}