package com.akopyan757.linkit.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.ClipboardUtils
import com.akopyan757.linkit.databinding.DialogNewUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.tab_folder.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClipboardUrlDialogFragment : BaseDialogFragment<DialogNewUrlBinding, LinkCreateUrlViewModel>() {

    override val viewModel: LinkCreateUrlViewModel by viewModel (
        parameters = { parametersOf(getClipboardUrlFromArguments()) }
    )

    override fun getLayoutId(): Int = R.layout.dialog_new_url
    override fun getVariableId(): Int = BR.viewModel
    override fun getStyleId() = R.style.CustomBottomSheetDialogTheme

    override fun onSetupView(bundle: Bundle?) {
        with(binding) {
            btnClipboardUrlAccept.setOnClickListener {
                this@ClipboardUrlDialogFragment.viewModel.createNewLink()
            }
        }
        with(viewModel) {
            getFolderLiveList().observe(viewLifecycleOwner) { folderNames ->
                updateFoldersList(folderNames)
            }

            startListenFolder()
            loadHtmlCards()
        }
    }

    override fun onAction(action: Int) {
        if (action == LinkCreateUrlViewModel.ACTION_DISMISS) {
            dismissDialog()
            clearClipboardData()
        }
    }

    private fun updateFoldersList(folders: List<FolderObservable>) {
        val inflater = LayoutInflater.from(requireContext())
        val tabLayout = binding.contentClipboard.cardTabLayoutFolder
        tabLayout.removeAllTabs()
        folders.forEach { observable ->
            val tab = tabLayout.newTab()
            val view = inflater.inflate(R.layout.tab_folder, tabLayout, false)
            view.tabFolder.text = observable.name
            tab.customView = view
            tabLayout.addTab(tab)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) viewModel.setSelectedFolderObservable(folders[tab.position])
            }
        })
    }


    private fun dismissDialog() {
        findNavController().popBackStack()
    }

    private fun clearClipboardData() {
        ClipboardUtils.clear(requireContext())
    }

    private fun getClipboardUrlFromArguments(): String? {
        return arguments?.getString(Config.CLIP_URL_LABEL)
    }
}