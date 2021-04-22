package com.akopyan757.linkit.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.ClipboardUtils
import com.akopyan757.linkit.databinding.DialogNewUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClipboardUrlDialogFragment : BaseDialogFragment<DialogNewUrlBinding, LinkCreateUrlViewModel>() {

    override val viewModel: LinkCreateUrlViewModel by viewModel (
        parameters = { parametersOf(getClipboardUrlFromArguments()) }
    )

    override fun getLayoutId(): Int = R.layout.dialog_new_url
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        with(binding) {
            btnClipboardUrlAccept.setOnClickListener {
                this@ClipboardUrlDialogFragment.viewModel.createNewLink()
            }
        }
        with(viewModel) {
            getFolderLiveList().observe(viewLifecycleOwner) { folderNames ->
                updateSpinnerFolderNames(folderNames)
            }

            startListenFolder()
            loadHtmlCards()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissDialog()
    }

    override fun onAction(action: Int) {
        if (action == LinkCreateUrlViewModel.ACTION_DISMISS) {
            dismissDialog()
            clearClipboardData()
        }
    }

    private fun updateSpinnerFolderNames(folderNames: List<String>) {
        val spinner = binding.contentClipboard.spCreateLinkAssignToFolder
        val spinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.item_folder_spinner, R.id.tvFolderSpinner, folderNames
        )
        spinner.adapter = spinnerAdapter
    }


    private fun dismissDialog() {
        findNavController().popBackStack()
    }

    private fun clearClipboardData() {
        ClipboardUtils.clear(requireContext())
    }

    private fun getClipboardUrlFromArguments(): String? {
        return arguments?.getString(Config.CLIP_URL_LABEL)
    }
}