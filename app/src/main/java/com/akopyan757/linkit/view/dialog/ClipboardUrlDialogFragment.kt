package com.akopyan757.linkit.view.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.DialogNewUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClipboardUrlDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "CLIPBOARD_UR_DF"
    }

    private lateinit var binding: DialogNewUrlBinding

    private val mViewModel: LinkCreateUrlViewModel by viewModel(
        parameters = { parametersOf(arguments?.getString(Config.CLIP_URL_LABEL)) }
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<DialogNewUrlBinding>(
                inflater, R.layout.dialog_new_url, container, false
        ).apply {
            viewModel = mViewModel
        }

        setupViewModel(mViewModel, viewLifecycleOwner)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().popBackStack()
    }

    private fun setupViewModel(viewModel: LinkCreateUrlViewModel, owner: LifecycleOwner) = with(
            viewModel
    ) {
        initResources(getString(R.string.notSelected))

        getFolderList().observe(owner, { holders ->
            val spinner = binding.contentClipboard.spCreateLinkAssignToFolder
            spinner.adapter = ArrayAdapter(
                requireContext(), R.layout.item_folder_spinner, R.id.tvFolderSpinner, holders.data
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