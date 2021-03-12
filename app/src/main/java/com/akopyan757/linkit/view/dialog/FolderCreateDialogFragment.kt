package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogNewFolderBinding
import com.akopyan757.linkit.viewmodel.FolderCreateViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FolderCreateDialogFragment : BaseDialogFragment<DialogNewFolderBinding, FolderCreateViewModel>() {

    override val mViewModel: FolderCreateViewModel by viewModel()

    override fun getLayoutId() = R.layout.dialog_new_folder
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: DialogNewFolderBinding, bundle: Bundle?) {
        binding.btnCreateLinkAccept.setOnClickListener { onCreateFolder() }
    }

    private fun onCreateFolder() {
        mViewModel.requestCreateFolder().apply {
            observeSuccessResponse { dismissDialog() }
            observeEmptyResponse { showError() }
        }
    }

    private fun dismissDialog() {
        findNavController().popBackStack()
    }

    private fun showError() {
        mViewModel.setErrorMessage(getString(R.string.notSelected))
    }
}