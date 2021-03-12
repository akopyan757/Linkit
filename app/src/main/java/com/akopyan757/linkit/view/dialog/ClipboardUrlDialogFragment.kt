package com.akopyan757.linkit.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.DialogNewUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClipboardUrlDialogFragment : BaseDialogFragment<DialogNewUrlBinding, LinkCreateUrlViewModel>() {

    override val mViewModel: LinkCreateUrlViewModel by viewModel (
        parameters = { parametersOf(getClipboardUrlFromArguments()) }
    )

    override fun getLayoutId(): Int = R.layout.dialog_new_url
    override fun getVariableId(): Int = BR.viewModel

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().popBackStack()
    }

    override fun onSetupView(binding: DialogNewUrlBinding, bundle: Bundle?) = with(binding) {

        btnClipboardUrlAccept.setOnClickListener {
            mViewModel.requestCreateNewLink()
        }

        mViewModel.bindFolderList()

        mViewModel.getFolderNameList().observe(viewLifecycleOwner, { names ->
            val spinner = mBinding.contentClipboard.spCreateLinkAssignToFolder
            spinner.adapter = ArrayAdapter(
                requireContext(), R.layout.item_folder_spinner, R.id.tvFolderSpinner, names
            )
        })
    }

    override fun onAction(action: Int) {
        if (action == LinkCreateUrlViewModel.ACTION_DISMISS) {
            findNavController().popBackStack()
            ClipboardUtils.clear(requireContext())
        }
    }

    private fun getClipboardUrlFromArguments(): String? {
        return arguments?.getString(Config.CLIP_URL_LABEL)
    }
}