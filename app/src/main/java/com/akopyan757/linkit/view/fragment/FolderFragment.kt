package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentFolderBinding
import com.akopyan757.linkit.view.adapter.FolderAdapter
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.view.callback.ItemTouchHelperCallback
import com.akopyan757.linkit.viewmodel.FolderViewModel
import com.akopyan757.linkit.viewmodel.listener.FolderClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.android.viewmodel.ext.android.viewModel

class FolderFragment : BaseFragment<FragmentFolderBinding, FolderViewModel>(), ItemTouchHelperAdapter {

    override val mViewModel: FolderViewModel by viewModel()

    private val mTouchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperCallback(this))
    }

    private val mAdapter: FolderAdapter by lazy {
        FolderAdapter(object : FolderClickListener {
            override fun onDeleteFolder(observable: FolderObservable) {
                mViewModel.onDeleteFolder(observable)
            }

            override fun onEditFolder(observable: FolderObservable) {
                //mViewModel.onEditFolder(observable)
            }

            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                mTouchHelper.startDrag(viewHolder)
            }
        })
    }

    private val mLayoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun getLayoutId() = R.layout.fragment_folder
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentFolderBinding, bundle: Bundle?): Unit = with(binding) {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            setHasOptionsMenu(true)
        }

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }

        rvFolders.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        mTouchHelper.attachToRecyclerView(rvFolders)

        fabCreateFolder.setOnClickListener {
            findNavController().navigate(R.id.action_folderFragment_to_createFolderDialogFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_folders, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemFoldersSave -> mViewModel.saveFolders()
        }
        return true
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val result = mAdapter.onItemMove(fromPosition, toPosition)
        mViewModel.setEditObservables(mAdapter.items)
        return result
    }

    override fun onSetupViewModel(viewModel: FolderViewModel) = with(mViewModel) {
        getFolderLiveListForSelect().observeList(mAdapter)
    }

    override fun onAction(action: Int): Unit = when (action) {
        FolderViewModel.ACTION_REORDER -> {
            findNavController().popBackStack(); Unit
        }

        else -> {}
    }
}