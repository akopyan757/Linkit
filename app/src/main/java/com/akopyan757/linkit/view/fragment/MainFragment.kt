package com.akopyan757.linkit.view.fragment

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.R
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.PageFragmentAdapter
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.sharedViewModel


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), ViewTreeObserver.OnWindowFocusChangeListener {

    companion object {
        private const val TAG = "MAIN_FRAGMENT"

        private const val PICK_FILE_REQUEST_CODE = 1111
    }

    override val mViewModel: LinkViewModel by sharedViewModel()

    private val mAdapter: PageFragmentAdapter by lazy {
        PageFragmentAdapter(requireActivity())
    }

    override fun getLayoutId() = R.layout.fragment_main

    override fun getVariableId() = BR.viewModel

    override fun onSetupView(binding: FragmentMainBinding, bundle: Bundle?): Unit = with(binding) {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            setTitle(R.string.app_name)
        }

        setHasOptionsMenu(true)

        viewPager.apply {
            adapter = mAdapter
        }

        TabLayoutMediator(tabLayoutFolder, viewPager) { tab, position ->
            tab.text = mAdapter.getItems()[position].name
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        view?.viewTreeObserver?.addOnWindowFocusChangeListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            Log.i(TAG, "HasFocus: $hasFocus")
            receiveUrlFromClipboard()
            view?.viewTreeObserver?.removeOnWindowFocusChangeListener(this)
        }
    }

    override fun onSetupViewModel(viewModel: LinkViewModel): Unit = with(viewModel) {
        getFolderLiveList().observe(viewLifecycleOwner, { holders ->
            mAdapter.updateFolders(holders.data)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.itemCreateFolder -> {
                findNavController().navigate(R.id.action_mainF_to_createFolderDF)
                true
            }

            R.id.itemAddFile -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "*/*"
                }
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
                true
            }

            else -> false
        }
    }

    private fun receiveUrlFromClipboard() {
        ClipboardUtils.getUrl(requireContext())?.also { url ->
            val bundle = Bundle().apply { putString(Config.CLIP_URL_LABEL, url) }
            findNavController().navigate(R.id.clipboardUrlDialogFragment, bundle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE_REQUEST_CODE) {
            data?.data?.also { uri ->
                //mViewModel.onAcceptUrl(uri.toString())
            }
        }
    }
}