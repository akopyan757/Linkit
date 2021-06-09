package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.DialogRenameFolderBinding
import com.akopyan757.linkit.viewmodel.FolderCreateViewModel
import com.akopyan757.linkit.viewmodel.FolderRenameViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FolderRenameDialogFragment : BaseDialogFragment<DialogRenameFolderBinding, FolderRenameViewModel>() {

    override val viewModel: FolderRenameViewModel by viewModel(
        parameters = { parametersOf(getFolderObservableFromArguments()) }
    )

    override fun getLayoutId() = R.layout.dialog_rename_folder
    override fun getVariableId(): Int = BR.viewModel
    override fun getStyleId() = R.style.CustomBottomSheetDialogTheme

    override fun onSetupView(bundle: Bundle?) {
        binding.btnRenameLinkAccept.setOnClickListener { renameFolder() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (binding.tietCreateLinkName.requestFocus()) {
            AndroidUtils.showKeyboard(requireActivity())
        }
    }

    private fun renameFolder() {
        val errorMessage = getString(R.string.notSelected)
        viewModel.requestRenameFolder(errorMessage)
    }

    override fun onAction(action: Int) {
        if (action == FolderCreateViewModel.ACTION_CLOSE) {
            dismissDialog()
        }
    }

    private fun dismissDialog() {
        findNavController().popBackStack()
    }

    private fun getFolderObservableFromArguments(): FolderObservable? {
        return arguments?.getSerializable(FolderDeleteDialogFragment.FOLDER_DATA) as? FolderObservable
    }

    companion object {
        const val OBSERVABLE = "FOLDER_DATA"
    }
}