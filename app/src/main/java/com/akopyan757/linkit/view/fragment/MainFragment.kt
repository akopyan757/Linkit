package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.ListChangeStrategy
import com.akopyan757.base.viewmodel.list.ListHolder
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.PageFragmentAdapter
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), ViewTreeObserver.OnWindowFocusChangeListener, KoinComponent {

    override val viewModel: LinkViewModel by viewModel()

    private lateinit var pageAdapter: PageFragmentAdapter

    override fun getLayoutId() = R.layout.fragment_main
    override fun getVariableId() = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        setupLifecycleOwner()
        setupActionBar()
        setupLinkViewPages()
        setupAdViews()
        binding.ivFolderSettings.setOnClickListener { openFolderDialog() }
        binding.tvFolderCreate.setOnClickListener { openCreateFolderDialog() }
        binding.ccvIconProfile.setOnClickListener { openProfileDialog() }
        viewModel.bindAllFoldersWithList()
        viewModel.requestInitResource().observeSuccessResponse {}
        viewModel.getFolderLiveList().observe(viewLifecycleOwner) {
            holder -> updateFoldersList(holder)
        }
        viewModel.getVisibleDeleteIcon().observe(viewLifecycleOwner) {
            visible -> updateDeleteIcon(visible)
        }
        viewModel.getSelectedCount().observe(viewLifecycleOwner) {
            count -> updateEditToolbar(count)
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeWindowFocusChanging()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            val clipboardUrl = ClipboardUtils.getUrl(requireContext())
            if (clipboardUrl != null) openCreateClipboardDialog(clipboardUrl)
            unsubscribeWindowFocusChanging()
        }
    }

    private fun updateFoldersList(folderHolder: ListHolder<FolderObservable>) {
        if (folderHolder.strategy is ListChangeStrategy.CustomChanged) {
            pageAdapter.updateFolders(folderHolder.data)
            folderHolder.strategy.after?.invoke()
        }
    }

    private fun updateDeleteIcon(iconVisible: Boolean) {
        binding.toolbarEdit.menu.setGroupVisible(R.id.groupEditSave, iconVisible)
    }

    private fun updateEditToolbar(countSelectedItems: Int) {
        val countName = if (countSelectedItems > 0) " ($countSelectedItems)" else Config.EMPTY
        binding.toolbarEdit.title = resources.getString(R.string.edit, countName)
    }

    private fun setupLifecycleOwner() {
        binding.lifecycleOwner = this@MainFragment
    }

    private fun setupActionBar() {
        setupMainToolbar()
        setupEditToolbar()
        setupAvatarPhoto()
    }

    private fun setupMainToolbar() {
        val appCompatActivity = requireActivity() as? AppCompatActivity ?: return
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        appCompatActivity.supportActionBar?.title = Config.EMPTY
        setHasOptionsMenu(true)
    }

    private fun setupEditToolbar() {
        val toolbar = binding.toolbarEdit
        toolbar.title = resources.getString(R.string.edit, Config.EMPTY)
        toolbar.menu.setGroupVisible(R.id.groupEditSave, false)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
        toolbar.setNavigationOnClickListener { viewModel.disableEditMode() }
        toolbar.inflateMenu(R.menu.menu_edit)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.itemEditDelete)
                viewModel.deleteSelected()
            true
        }
    }

    private fun setupAvatarPhoto() {
        viewModel.requestGetUserAvatar().observeSuccessResponse {}
    }

    private fun setupLinkViewPages() {
        val tabLayout = binding.tabLayoutFolder
        val viewPager = binding.viewPager
        pageAdapter = PageFragmentAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = pageAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = pageAdapter.getTabView(tabLayout, position)
        }.attach()
        tabLayout.offsetLeftAndRight(ZERO_OFFSET)
    }

    private fun setupAdViews() {
        BannerViewExtension.loadAd(binding.bannerBottomAd) {
            binding.bannerBottomAd.visibility = View.GONE
        }
    }

    private fun subscribeWindowFocusChanging() {
        view?.viewTreeObserver?.removeOnWindowFocusChangeListener(this)
    }

    private fun unsubscribeWindowFocusChanging() {
        view?.viewTreeObserver?.removeOnWindowFocusChangeListener(this)
    }

    private fun openCreateClipboardDialog(url: String) {
        val bundle = Bundle().apply { putString(Config.CLIP_URL_LABEL, url) }
        findNavController().navigate(R.id.clipboardUrlDialogFragment, bundle)
    }

    private fun openFolderDialog() {
        findNavController().navigate(R.id.action_mainFragment_to_folderFragment)
    }

    private fun openCreateFolderDialog() {
        findNavController().navigate(R.id.folderCreateDialogFragment)
    }

    private fun openProfileDialog() {
        findNavController().navigate(R.id.action_mainFragment_to_profileDialogFragment)
    }

    companion object {
        private const val ZERO_OFFSET = 0
    }
}