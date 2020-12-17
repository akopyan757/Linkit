package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentPageBinding
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.view.callback.ItemTouchHelperCallback
import com.akopyan757.linkit.viewmodel.PageViewModel
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PageFragment: BaseFragment<FragmentPageBinding, PageViewModel>(), LinkAdapterListener, ItemTouchHelperAdapter {

    override val mViewModel: PageViewModel by viewModel { parametersOf(mObservable.id) }

    private lateinit var mObservable: FolderObservable

    private val mTouchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperCallback(this))
    }

    override fun getVariableId() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mObservable = arguments?.getSerializable(TAG_FOLDER) as? FolderObservable ?: return
    }

    override fun getLayoutId() = R.layout.fragment_page

    private val mUrlAdapter: LinkUrlAdapter by lazy {
        LinkUrlAdapter(this)
    }

    override fun onSetupView(binding: FragmentPageBinding, bundle: Bundle?) = with(binding) {

        val urlLayoutManager = when (mObservable.type) {
            1 -> LinearLayoutManagerWrapper(requireContext())
            else -> GridLayoutManager(requireContext(), 2)
        }

        fragmentWebLinkList.apply {
            adapter = mUrlAdapter
            layoutManager = urlLayoutManager
        }

        mTouchHelper.attachToRecyclerView(fragmentWebLinkList)
    }

    override fun onSetupViewModel(viewModel: PageViewModel) = with(viewModel) {
        getUrlLiveList().observeList(mUrlAdapter)
        getLiveEditMode().observe(viewLifecycleOwner, { editMode ->
            Log.i(TAG, "editMode = $editMode")
            mUrlAdapter.setEditMode(editMode)
        })
    }

    override fun onShareListener(link: LinkObservable, editMode: Boolean) {
        if (!editMode) {
            startActivity(AndroidUtils.createShareIntent(link.url, link.title))
        }
    }

    override fun onItemListener(link: LinkObservable, editMode: Boolean) {
        if (editMode) {
            mViewModel.selectItem(link)
        } else {
            startActivity(AndroidUtils.createIntent(link.url))
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val result = mUrlAdapter.onItemMove(fromPosition, toPosition)
        mViewModel.setEditObservables(mUrlAdapter.items)
        return result
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mTouchHelper.startDrag(viewHolder)
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