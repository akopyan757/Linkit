package com.akopyan757.linkit.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import kotlinx.android.synthetic.main.dialog_folder_delete.view.*

class FolderDeleteDialogFragment: DialogFragment() {

    private val savedStateHandle: SavedStateHandle? by lazy {
        findNavController().previousBackStackEntry?.savedStateHandle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val folder = arguments?.getSerializable(FOLDER_DATA) as? FolderObservable
        return if (folder != null) {
            val text = getString(R.string.delete_folder_description, folder.name)
            val view = View.inflate(context, R.layout.dialog_folder_delete, null).apply {
                tvFolderDeleteDescription.text = text
                btnFolderDeleteAccept.setOnClickListener {
                    savedStateHandle?.set(Config.KEY_ACCEPT_DELETE, folder)
                    dismiss()
                }
                btnFolderDeleteCancel.setOnClickListener { dismiss() }
            }

            AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog)
                .setView(view)
                .create()
        } else {
            super.onCreateDialog(savedInstanceState)
        }
    }

    companion object {
        const val FOLDER_DATA = "FOLDER_DATA"
    }
}