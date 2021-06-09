package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_folder_delete.view.*

class FolderDeleteDialogFragment: BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val folderObservable = getFolderObservableFromArguments() ?: return null
        val dialogView = View.inflate(context, R.layout.dialog_folder_delete, container)
        val dialogDescription = getString(R.string.delete_folder_description, folderObservable.name)
        dialogView.tvFolderDeleteDescription.text = dialogDescription
        dialogView.btnFolderDeleteAccept.setOnClickListener { acceptFolder(folderObservable) }
        dialogView.btnFolderDeleteCancel.setOnClickListener { dismiss() }
        return dialogView
    }

    private fun acceptFolder(folder: FolderObservable) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(Config.KEY_ACCEPT_DELETE, folder)
        findNavController().popBackStack()
    }

    private fun getFolderObservableFromArguments(): FolderObservable? {
        return arguments?.getSerializable(FOLDER_DATA) as? FolderObservable
    }

    companion object {
        const val FOLDER_DATA = "FOLDER_DATA"
    }
}