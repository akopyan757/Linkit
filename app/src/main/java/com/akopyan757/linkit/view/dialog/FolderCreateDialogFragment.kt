package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogNewFolderBinding
import com.akopyan757.linkit.viewmodel.FolderCreateViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FolderCreateDialogFragment : BaseDialogFragment<DialogNewFolderBinding, FolderCreateViewModel>() {

    override val viewModel: FolderCreateViewModel by viewModel()

    override fun getLayoutId() = R.layout.dialog_new_folder
    override fun getVariableId(): Int = BR.viewModel
    override fun getStyleId() = R.style.CustomBottomSheetDialogTheme

    override fun onSetupView(bundle: Bundle?) {
        binding.btnCreateLinkAccept.setOnClickListener { createFolder() }
    }

    private fun createFolder() {
        val errorMessage = getString(R.string.notSelected)
        viewModel.requestCreateFolder(errorMessage)
    }

    override fun onAction(action: Int) {
        if (action == FolderCreateViewModel.ACTION_CLOSE)
            dismissDialog()
    }

    private fun dismissDialog() {
        findNavController().popBackStack()
    }
}