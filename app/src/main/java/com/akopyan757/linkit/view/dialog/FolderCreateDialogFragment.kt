package com.akopyan757.linkit.view.dialog

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogNewFolderBinding
import com.akopyan757.linkit.viewmodel.FolderCreateViewModel
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FolderCreateDialogFragment : BaseDialogFragment<DialogNewFolderBinding, FolderCreateViewModel>() {

    override val mViewModel: FolderCreateViewModel by viewModel()

    override fun getLayoutId() = R.layout.dialog_new_folder

    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupViewModel(
        viewModel: FolderCreateViewModel,
        owner: LifecycleOwner
    ) = with(viewModel) {
        initResources(getString(R.string.notSelected))

        getLiveResponses().observe(viewLifecycleOwner, { state ->
            if (state == BaseViewModel.RequestState.SUCCESS) {
                findNavController().popBackStack()
            }
        })
    }
}