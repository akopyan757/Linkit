package com.akopyan757.linkit.view.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.viewmodel.FolderSelectViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import kotlinx.android.synthetic.main.dialog_select_folder.*
import kotlinx.android.synthetic.main.dialog_select_folder.view.*
import kotlinx.android.synthetic.main.item_folder_select.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SelectFolderDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "SELECT_FOLDER_DF"
    }

    private val viewModel: FolderSelectViewModel by viewModel()

    private val prevSavedStateHandle: SavedStateHandle? by lazy {
        findNavController().previousBackStackEntry?.savedStateHandle
    }

    private val inflater : LayoutInflater by lazy {
        LayoutInflater.from(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_select_folder, container, false)

        view.tvSelectFolderCreate.setOnClickListener {
            findNavController().navigate(R.id.action_selectFolderDF_to_createFolderDF)
        }

        view.btnSelectFolderAdd.setOnClickListener {
            val checked = (0 until llSelectFolderList.size).map { index ->
                llSelectFolderList[index].cbSelectFolderName.isChecked
            }
            val observables = viewModel.getFolderLiveListForSelect()
                .value?.data?.mapIndexedNotNull { index, observable ->
                    if (checked.getOrElse(index) { false } ) observable else null
                }

            prevSavedStateHandle?.set(Config.TAG_SELECT_FOLDER, observables)
        }

        viewModel.initResources(getString(R.string.notSelected))

        val checkedIds = prevSavedStateHandle
            ?.get<List<FolderObservable>>(Config.TAG_SELECT_FOLDER)
            ?.map { it.id } ?: emptyList()

        viewModel.getFolderLiveListForSelect().observe(viewLifecycleOwner, { holder ->
            holder.data.forEach {
                val view = createCheckBox(it, it.id in checkedIds)
                llSelectFolderList.addView(view)
            }
        })

        viewModel.getLiveResponses().observe(viewLifecycleOwner, {
            Log.i(TAG, "Response = $it")
        })

        return view
    }

    @SuppressLint("InflateParams")
    private fun createCheckBox(folderObservable: FolderObservable, isSelected: Boolean): View {
        return inflater.inflate(R.layout.item_folder_select, null, false).also {
            it.cbSelectFolderName.text = folderObservable.name
            it.cbSelectFolderName.isChecked = isSelected
        }
    }
}