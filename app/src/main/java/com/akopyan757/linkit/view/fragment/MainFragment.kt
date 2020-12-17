package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.PageFragmentAdapter
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), ViewTreeObserver.OnWindowFocusChangeListener {

    override val mViewModel: LinkViewModel by sharedViewModel()

    private lateinit var mAdapter: PageFragmentAdapter

    override fun getLayoutId() = R.layout.fragment_main

    override fun getVariableId() = BR.viewModel

    override fun onSetupView(binding: FragmentMainBinding, bundle: Bundle?): Unit = with(binding) {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            setTitle(R.string.app_name)
        }

        lifecycleOwner = this@MainFragment

        setHasOptionsMenu(true)

        toolbarEdit.apply {
            title = resources.getString(R.string.edit, Config.EMPTY)
            menu.setGroupVisible(R.id.groupEditSave, false)
            setNavigationIcon(R.drawable.ic_baseline_close_24)
            setNavigationOnClickListener {
                mViewModel.disableEditMode()

            }
            inflateMenu(R.menu.menu_edit)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.itemEditSave -> mViewModel.saveEdit()
                }
                true
            }
        }

        mAdapter = PageFragmentAdapter(requireActivity())
        viewPager.adapter = mAdapter

        TabLayoutMediator(tabLayoutFolder, viewPager) { tab, position ->
            tab.customView = mAdapter.getTabView(position)
        }.attach()

        tabLayoutFolder.offsetLeftAndRight(0)
    }

    override fun onStart() {
        super.onStart()
        view?.viewTreeObserver?.addOnWindowFocusChangeListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            ClipboardUtils.getUrl(requireContext())?.also { url ->
                val bundle = Bundle().apply { putString(Config.CLIP_URL_LABEL, url) }
                findNavController().navigate(R.id.clipboardUrlDialogFragment, bundle)
            }

            view?.viewTreeObserver?.removeOnWindowFocusChangeListener(this)
        }
    }

    override fun onSetupViewModel(viewModel: LinkViewModel): Unit = with(viewModel) {
        getFolderLiveList().observe(viewLifecycleOwner, { holders ->
            mAdapter.updateFolders(holders.data)
        })
        getDeleteIconVisible().observe(viewLifecycleOwner, { deleteVisible ->
            toolbarEdit.menu.setGroupVisible(R.id.groupEditSave, deleteVisible)
        })
        getSelectedCount().observe(viewLifecycleOwner, { count ->
            val countName = count.takeIf { it > 0 }?.let { " ($it)" } ?: Config.EMPTY
            toolbarEdit.title = resources.getString(R.string.edit, countName)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.itemFolders -> {
                findNavController().navigate(R.id.action_mainFragment_to_folderFragment)
                true
            }

            R.id.itemEditFolder -> {
                mViewModel.enableEditMode()
                true
            }

            else -> false
        }
    }
}