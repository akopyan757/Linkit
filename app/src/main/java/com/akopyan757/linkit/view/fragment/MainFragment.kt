package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), LinkAdapterListener,
    ViewTreeObserver.OnWindowFocusChangeListener, KoinComponent {

    override val viewModel: LinkViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_main
    override fun getVariableId() = BR.viewModel

    private lateinit var recyclerLinksAdapter: LinkUrlAdapter

    override fun onSetupView(bundle: Bundle?) {
        setupLifecycleOwner()
        setupActionBar()
        setupLinkRecyclerView()
        setupAdViews()
        binding.ivFolderSettings.setOnClickListener { openFolderDialog() }
        //binding.tvFolderCreate.setOnClickListener { openCreateFolderDialog() } TODO
        binding.ccvIconProfile.setOnClickListener { openProfileDialog() }

        with(viewModel) {
            listenFolders().observe(viewLifecycleOwner) { folderObservable ->
                val names = folderObservable.map { observable -> observable.name }
                binding.spinnerSelectedFolder?.adapter = ArrayAdapter(
                    requireContext(), R.layout.item_folder_spinner, R.id.tvFolderSpinner, names
                )
            }
            listenLinks().observe(viewLifecycleOwner) {}
            requestFetchRemoteData().observeSuccessResponse {}
            linkListLive().observeList(recyclerLinksAdapter)
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

    private fun setupLifecycleOwner() {
        binding.lifecycleOwner = this@MainFragment
    }

    private fun setupActionBar() {
        setupMainToolbar()
        setupAvatarPhoto()
    }

    private fun setupMainToolbar() {
        val appCompatActivity = requireActivity() as? AppCompatActivity ?: return
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        appCompatActivity.supportActionBar?.title = Config.EMPTY
        setHasOptionsMenu(true)
    }

    private fun setupAvatarPhoto() {
        viewModel.requestGetUserAvatar().observeSuccessResponse {}
    }

    private fun setupLinkRecyclerView() {
        recyclerLinksAdapter = LinkUrlAdapter(this)
        binding.rvLinks?.apply {
            adapter = recyclerLinksAdapter
            layoutManager = LinearLayoutManagerWrapper(requireContext())
        }
    }

    override fun onShareListener(link: LinkObservable) {
        startActivity(AndroidUtils.createShareIntent(link.url, link.title))
    }

    override fun onItemListener(link: LinkObservable) {
        openPreviewOfItem(link)
        viewModel.requestMoveLinkToTop(link)
    }

    override fun onItemLongClickListener(link: LinkObservable) {

    }

    override fun onAdClosed(adObservable: AdObservable) {
        viewModel.closeAdItem(adObservable)
    }

    private fun setupAdViews() {
        BannerViewExtension.loadAd(binding.bannerBottomAd) {
            binding.bannerBottomAd.visibility = View.GONE
        }
    }

    private fun subscribeWindowFocusChanging() {
        view?.viewTreeObserver?.addOnWindowFocusChangeListener(this)
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

    private fun openPreviewOfItem(observable: LinkObservable) {
        val bundle = bundleOf(PreviewUrlFragment.PREVIEW_URL to observable)
        findNavController().navigate(R.id.action_mainFragment_to_preview, bundle)
    }
}