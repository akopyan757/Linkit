package com.akopyan757.linkit.view.dialog

import android.content.DialogInterface
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
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

    companion object {
        private const val TAG = "CLIPBOARD_UR_DF"
    }

    override val mViewModel: LinkCreateUrlViewModel by viewModel (
        parameters = { parametersOf(arguments?.getString(Config.CLIP_URL_LABEL)) }
    )

    override fun getLayoutId(): Int = R.layout.dialog_new_url
    override fun getVariableId(): Int = BR.viewModel

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().popBackStack()
    }

    override fun onSetupViewModel(
        viewModel: LinkCreateUrlViewModel,
        owner: LifecycleOwner
    ) = with(viewModel){

        initResources(getString(R.string.notSelected))

        val spinner = mBinding.contentClipboard.spCreateLinkAssignToFolder

        getFolderNameList().observe(owner, { names ->
            spinner.adapter = ArrayAdapter(
                requireContext(), R.layout.item_folder_spinner, R.id.tvFolderSpinner, names
            )
        })

        getLiveResponses().observe(owner, {
            Log.i(TAG, "OBSERVER")
        })

        getLiveAction().observe(owner, { actionId ->
            when (actionId) {
                LinkCreateUrlViewModel.ACTION_DISMISS -> {
                    findNavController().popBackStack()
                    ClipboardUtils.clear(requireContext())
                }
            }
        })
    }

}