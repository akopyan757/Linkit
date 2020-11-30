package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.databinding.FragmentPage1Binding
import com.akopyan757.linkit.databinding.FragmentPage2Binding
import com.akopyan757.linkit.view.adapter.LinkStoreAdapter
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.PageViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PageFragment: BaseFragment<ViewDataBinding, PageViewModel>() {

    override val mViewModel: PageViewModel by viewModel { parametersOf(mObservable.id) }

    private lateinit var mObservable: FolderObservable

    override fun getVariableId() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mObservable = arguments?.getSerializable(TAG_FOLDER) as? FolderObservable ?: return
    }

    override fun getLayoutId() = when (mObservable.type) {
        1 -> R.layout.fragment_page_1
        else -> R.layout.fragment_page_2
    }

    private val mUrlAdapter: LinkUrlAdapter by lazy {
        when (mObservable.type) {
            1 -> LinkUrlAdapter(LinkUrlAdapter.Type.ITEM)
            else -> LinkUrlAdapter(LinkUrlAdapter.Type.BOX)
        }
    }

    private val mStoreAdapter: LinkStoreAdapter by lazy {
        LinkStoreAdapter()
    }

    private val mUrlLayoutManager: LinearLayoutManagerWrapper by lazy {
        when (mObservable.type) {
            1 -> LinearLayoutManagerWrapper(requireContext())
            else -> LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private val mStoreLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(requireContext(), 2)
    }

    override fun onSetupView(binding: ViewDataBinding, bundle: Bundle?) {
        val urlRecyclerView = when (binding) {
            is FragmentPage1Binding -> binding.fragmentWebLinkList
            is FragmentPage2Binding -> binding.fragmentWebLinkList
            else -> null
        }

        val storeRecycleView = when (binding) {
            is FragmentPage1Binding -> binding.fragmentStoreLinkList
            else -> null
        }

        urlRecyclerView?.apply {
            adapter = mUrlAdapter
            layoutManager = mUrlLayoutManager
        }

        storeRecycleView?.apply {
            adapter = mStoreAdapter
            layoutManager = mStoreLayoutManager
        }
    }

    override fun onSetupViewModel(viewModel: PageViewModel) = with(viewModel) {
        getUrlLiveList().observeList(mUrlAdapter)
        getStoreLiveList().observeList(mStoreAdapter)
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