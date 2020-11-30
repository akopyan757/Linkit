package com.akopyan757.linkit.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.base.viewmodel.list.observeList
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.DialogNewUrlBinding
import com.akopyan757.linkit.view.adapter.LinkFoldersAdapter
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ClipboardUrlDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "CLIPBOARD_UR_DF"
    }

    private lateinit var binding: DialogNewUrlBinding

    private val mViewModel: LinkCreateUrlViewModel by sharedViewModel()

    private val mAdapter: LinkFoldersAdapter by lazy {
        LinkFoldersAdapter(onAddFolders = {
            findNavController().navigate(R.id.action_clipboardUrlDF_to_selectFolderDF)
        })
    }

    private val currentSavedStateHandle: SavedStateHandle? by lazy {
        findNavController().currentBackStackEntry?.savedStateHandle
    }

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

        binding.btnClipboardUrlAccept.setOnClickListener {
            mViewModel.onAcceptUrl()
        }

        binding.rvCreateListFolders.apply {
            adapter = mAdapter
            layoutManager = FlexboxLayoutManager(requireContext())
        }


        currentSavedStateHandle
            ?.getLiveData<List<FolderObservable>>(Config.TAG_SELECT_FOLDER)
            ?.observe(viewLifecycleOwner, { observables ->
                mViewModel.setFolderList(observables)
            })

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

        arguments?.getString(Config.CLIP_URL_LABEL)?.also { url ->
            setupUrl(url)
        }

        getFolderLiveList().observeList(owner, mAdapter)

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