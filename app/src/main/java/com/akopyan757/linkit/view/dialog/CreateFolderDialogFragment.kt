package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogNewFolderBinding
import com.akopyan757.linkit.viewmodel.FolderCreateViewModel
import com.akopyan757.base.viewmodel.BaseViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CreateFolderDialogFragment : DialogFragment() {

    private val mViewModel: FolderCreateViewModel by viewModel()

    private lateinit var binding: DialogNewFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<DialogNewFolderBinding>(
            inflater, R.layout.dialog_new_folder, container, false
        ).apply {
            viewModel = mViewModel
        }

        mViewModel.initResources(getString(R.string.notSelected))

        mViewModel.getLiveResponses().observe(viewLifecycleOwner, { state ->
            if (state == BaseViewModel.RequestState.SUCCESS) {
                findNavController().navigate(R.id.action_createFolderDF_to_clipboardUrlDF)
            }
        })

        return binding.root
    }
}