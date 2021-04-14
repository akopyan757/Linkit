package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.databinding.DialogFoldersSettingsBinding
import com.akopyan757.linkit.view.adapter.FolderAdapter
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.view.callback.FolderTouchCallback
import com.akopyan757.linkit.viewmodel.FolderViewModel
import com.akopyan757.linkit.viewmodel.listener.FolderClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.android.viewmodel.ext.android.viewModel

class FolderDialogFragment : BaseDialogFragment<DialogFoldersSettingsBinding, FolderViewModel>(),
        ItemTouchHelperAdapter, FolderClickListener {

    override val viewModel: FolderViewModel by viewModel()

    private lateinit var recyclerTouchHelper: ItemTouchHelper
    private lateinit var recyclerAdapter: FolderAdapter
    private lateinit var recyclerLayoutManager: RecyclerView.LayoutManager

    override fun getLayoutId() = R.layout.dialog_folders_settings
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        setupRecyclerView()
        binding.btnCreateFolder.setOnClickListener { openCreateFolderScreen() }
        binding.btnFoldersAccept.setOnClickListener { viewModel.reorderFolder() }

        viewModel.startListenFolders()
        viewModel.getFolderLiveListForSelect().observeList(recyclerAdapter)

        observeDeleteAcceptState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_folders, menu)
    }

    override fun onDeleteFolder(observable: FolderObservable) {
        openDeleteFolderDialog(observable)
    }

    override fun onEditFolder(observable: FolderObservable) {
        viewModel.renameFolder(observable)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        recyclerTouchHelper.startDrag(viewHolder)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val result = recyclerAdapter.onItemMove(fromPosition, toPosition)
        viewModel.setEditObservables(recyclerAdapter.items)
        return result
    }

    override fun onAction(action: Int) {
        if (action == FolderViewModel.ACTION_DISMISS) {
            dismissDialog()
        }
    }

    private fun setupRecyclerView() {
        recyclerAdapter = FolderAdapter(this)
        recyclerLayoutManager = LinearLayoutManager(requireContext())
        val folderRecyclerView = binding.rvFolders
        folderRecyclerView.adapter = recyclerAdapter
        folderRecyclerView.layoutManager = recyclerLayoutManager
        recyclerTouchHelper = ItemTouchHelper(FolderTouchCallback(this))
        recyclerTouchHelper.attachToRecyclerView(folderRecyclerView)
    }

    private fun observeDeleteAcceptState() {
        val savedStateHandle = getDeleteStateHandle() ?: return
        val liveData = savedStateHandle.getLiveData<FolderObservable>(Config.KEY_ACCEPT_DELETE)
        liveData.observe(viewLifecycleOwner, { folderObservable ->
            viewModel.deleteFolder(folderObservable.id)
        })
    }

    private fun dismissDialog() {
        findNavController().popBackStack()
    }

    private fun getDeleteStateHandle(): SavedStateHandle? {
        return findNavController().currentBackStackEntry?.savedStateHandle
    }

    private fun openCreateFolderScreen() {
        findNavController().navigate(R.id.action_folderFragment_to_create)
    }

    private fun openDeleteFolderDialog(observable: FolderObservable) {
        val bundle = bundleOf(FolderDeleteDialogFragment.FOLDER_DATA to observable)
        findNavController().navigate(R.id.action_folderFragment_to_delete, bundle)
    }
}