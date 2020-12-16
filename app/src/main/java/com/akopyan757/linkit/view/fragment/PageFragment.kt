package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
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

class PageFragment: BaseFragment<ViewDataBinding, PageViewModel>(), LinkAdapterListener {

    override val mViewModel: PageViewModel by viewModel { parametersOf(mObservable.id) }

    private lateinit var mObservable: FolderObservable

    private val mTouchHelper: ItemTouchHelper by lazy {
        val callback = ItemTouchHelperCallback(object : ItemTouchHelperAdapter{
            override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
                val result = mUrlAdapter.onItemMove(fromPosition, toPosition)
                mViewModel.setEditObservables(mUrlAdapter.items)
                return result
            }
        })
        ItemTouchHelper(callback)
    }

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

        mTouchHelper.attachToRecyclerView(urlRecyclerView)
    }

    override fun onSetupViewModel(viewModel: PageViewModel) = with(viewModel) {
        getUrlLiveList().observeList(mUrlAdapter)
        getLiveEditMode().observe(viewLifecycleOwner, { editMode ->
            Log.i(TAG, "editMode = $editMode")
            mUrlAdapter.setEditMode(editMode)
        })
    }

    override fun onShareListener(link: LinkObservable) {
        startActivity(AndroidUtils.createShareIntent(link.url, link.title))
    }

    override fun onItemListener(link: LinkObservable) {
        startActivity(AndroidUtils.createIntent(link.url))
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