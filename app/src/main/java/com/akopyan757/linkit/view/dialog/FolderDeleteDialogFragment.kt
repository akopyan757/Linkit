package com.akopyan757.linkit.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import kotlinx.android.synthetic.main.dialog_folder_delete.view.*

class FolderDeleteDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val folderObservable = getFolderObservableFromArguments()
                ?: return super.onCreateDialog(savedInstanceState)
        val dialogView = createDialogViewFromFolder(folderObservable)
        return createCustomDialog(dialogView)
    }

    private fun createCustomDialog(view: View): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_Linkit_AlertDialog)
                .setView(view)
                .create()
    }

    private fun createDialogViewFromFolder(folder: FolderObservable): View {
        val dialogView = View.inflate(context, R.layout.dialog_folder_delete, null)
        val dialogDescription = getString(R.string.delete_folder_description, folder.name)
        dialogView.tvFolderDeleteDescription.text = dialogDescription
        dialogView.btnFolderDeleteAccept.setOnClickListener { acceptFolder(folder) }
        dialogView.btnFolderDeleteCancel.setOnClickListener { dismiss() }
        return dialogView
    }

    private fun acceptFolder(folder: FolderObservable) {
        emitEventDeleteFolder(folder)
        dismiss()
    }

    private fun emitEventDeleteFolder(folder: FolderObservable) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(Config.KEY_ACCEPT_DELETE, folder)
    }

    private fun getFolderObservableFromArguments(): FolderObservable? {
        return arguments?.getSerializable(FOLDER_DATA) as FolderObservable
    }

    companion object {
        const val FOLDER_DATA = "FOLDER_DATA"
    }
}