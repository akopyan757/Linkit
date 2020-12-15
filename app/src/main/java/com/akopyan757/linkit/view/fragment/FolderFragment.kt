package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentFolderBinding
import com.akopyan757.linkit.view.adapter.FolderAdapter
import com.akopyan757.linkit.viewmodel.FolderViewModel
import com.akopyan757.linkit.viewmodel.listener.FolderClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.android.viewmodel.ext.android.viewModel

class FolderFragment : BaseFragment<FragmentFolderBinding, FolderViewModel>() {

    override val mViewModel: FolderViewModel by viewModel()

    private val mAdapter: FolderAdapter by lazy {
        FolderAdapter(object : FolderClickListener {
            override fun onDeleteFolder(observable: FolderObservable) {
                mViewModel.onDeleteFolder(observable)
            }

            override fun onEditFolder(observable: FolderObservable) {
                //mViewModel.onEditFolder(observable)
            }
        })
    }

    private val mLayoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun getLayoutId() = R.layout.fragment_folder
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentFolderBinding, bundle: Bundle?): Unit = with(binding) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        rvFolders.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
    }

    override fun onSetupViewModel(viewModel: FolderViewModel) = with(mViewModel) {
        getFolderLiveListForSelect().observeList(mAdapter)
    }
}