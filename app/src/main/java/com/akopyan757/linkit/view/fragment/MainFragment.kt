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
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), ViewTreeObserver.OnWindowFocusChangeListener, KoinComponent {

    override val mViewModel: LinkViewModel by viewModel()

    private val firebaseAuth: FirebaseAuth by mainInject()
    private lateinit var pageAdapter: PageFragmentAdapter

    override fun getLayoutId() = R.layout.fragment_main
    override fun getVariableId() = BR.viewModel

    override fun onSetupView(binding: FragmentMainBinding, bundle: Bundle?) = with(binding) {
        setupLifecycleOwner()
        setupMainToolbar()
        setupEditToolbar()
        setupAvatarPhoto()
        setupLinkPagesAdapter()
        setupAdViews()
        ivFolderSettings.setOnClickListener { openFolderDialog() }
        tvFolderCreate.setOnClickListener { openCreateFolderDialog() }
        ccvIconProfile.setOnClickListener { openProfileDialog() }
        mViewModel.bindAllFoldersWithList()
        mViewModel.requestInitResource().observeSuccessResponse {}
        observeStates(mViewModel.getFolderLiveList()) { holder -> updateFoldersList(holder) }
        observeStates(mViewModel.getVisibleDeleteIcon()) { visible -> updateDeleteIcon(visible) }
        observeStates(mViewModel.getSelectedCount()) { count -> updateEditToolbar(count) }
    }


    override fun onStart() {
        super.onStart()
        subscribeWindowFocusChanging()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            val clipboardUrl = ClipboardUtils.getUrl(requireContext())
            if (clipboardUrl != null)
                openCreateClipboardDialog(clipboardUrl)
            unsubscribeWindowFocusChanging()
        }
    }

    private fun <T> observeStates(liveState: LiveData<T>, onObserve: (T) -> Unit) {
        liveState.observe(viewLifecycleOwner, { data ->
            onObserve.invoke(data)
        })
    }

    private fun updateFoldersList(folderHolder: ListHolder<FolderObservable>) {
        if (folderHolder.strategy is ListChangeStrategy.CustomChanged) {
            pageAdapter.updateFolders(folderHolder.data)
            folderHolder.strategy.after?.invoke()
        }
    }

    private fun updateDeleteIcon(iconVisible: Boolean) {
        mBinding.toolbarEdit.menu.setGroupVisible(R.id.groupEditSave, iconVisible)
    }

    private fun updateEditToolbar(countSelectedItems: Int) {
        val countName = if (countSelectedItems > 0) " ($countSelectedItems)" else Config.EMPTY
        mBinding.toolbarEdit.title = resources.getString(R.string.edit, countName)
    }

    private fun setupLifecycleOwner() {
        mBinding.lifecycleOwner = this@MainFragment
    }

    private fun setupMainToolbar() {
        val appCompatActivity = requireActivity() as? AppCompatActivity ?: return
        appCompatActivity.setSupportActionBar(mBinding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        appCompatActivity.supportActionBar?.title = Config.EMPTY
        setHasOptionsMenu(true)
    }

    private fun setupEditToolbar() {
        val toolbar = mBinding.toolbarEdit
        toolbar.title = resources.getString(R.string.edit, Config.EMPTY)
        toolbar.menu.setGroupVisible(R.id.groupEditSave, false)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
        toolbar.setNavigationOnClickListener { mViewModel.disableEditMode() }
        toolbar.inflateMenu(R.menu.menu_edit)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.itemEditDelete)
                mViewModel.deleteSelected()
            true
        }
    }

    private fun setupLinkPagesAdapter() {
        val tabLayout = mBinding.tabLayoutFolder
        val viewPager = mBinding.viewPager
        pageAdapter = PageFragmentAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = pageAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = pageAdapter.getTabView(tabLayout, position)
        }.attach()
        tabLayout.offsetLeftAndRight(ZERO_OFFSET)
    }

    private fun setupAvatarPhoto() {
        val photoUrl = firebaseAuth.currentUser?.photoUrl?.toString()
        if (photoUrl != null)
            mViewModel.setProfileUrl(photoUrl)
    }

    private fun setupAdViews() {
        BannerViewExtension.loadAd(mBinding.bannerBottomAd) {
            mBinding.bannerBottomAd.visibility = View.GONE
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