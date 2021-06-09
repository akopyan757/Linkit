package com.akopyan757.linkit.view.fragment

import android.app.AlertDialog
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.LinearLayoutManagerWrapper
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.common.utils.ClipboardUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.LinkUrlAdapter
import com.akopyan757.linkit.view.dialog.FolderDeleteDialogFragment
import com.akopyan757.linkit.view.dialog.FolderRenameDialogFragment
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.BaseLinkObservable
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkAppObservable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.dialog_folder_setting.view.*
import kotlinx.android.synthetic.main.tab_folder.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), LinkAdapterListener,
    ViewTreeObserver.OnWindowFocusChangeListener, KoinComponent {

    override val viewModel: LinkViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_main
    override fun getVariableId() = BR.viewModel

    private lateinit var recyclerLinksAdapter: LinkUrlAdapter
    private var tabLayoutListener: TabLayout.OnTabSelectedListener? = null

    override fun onSetupView(bundle: Bundle?) {
        setupLifecycleOwner()
        setupActionBar()
        setupLinkRecyclerView()
        setupAdViews()
        observeDeleteAcceptState()
        binding.ccvIconProfile.setOnClickListener { openProfileDialog() }

        with(viewModel) {
            startListenDataChanges()
            startListenFolders()
            listenLinksById(null)
            listenFolderNames().observe(viewLifecycleOwner) { names -> updateFoldersList(names) }
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
        setupEditToolbar()
        viewModel.getUserAvatar()
    }

    private fun setupMainToolbar() {
        val appCompatActivity = requireActivity() as? AppCompatActivity ?: return
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        appCompatActivity.supportActionBar?.title = Config.EMPTY
        setHasOptionsMenu(true)
    }

    private fun setupEditToolbar() {
        binding.toolbarEdit?.apply {
            setNavigationIcon(R.drawable.ic_baseline_close_24)
            setNavigationOnClickListener { viewModel.closeEditMode() }
            inflateMenu(R.menu.menu_edit)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itemAssignFolder -> { showFolderChoiceForAssignLinks(); true }
                    R.id.itemEditDelete -> { showAcceptDeleteAction(); true }
                    else -> super.onOptionsItemSelected(menuItem)
                }
            }
        }
    }

    private fun setupLinkRecyclerView() {
        recyclerLinksAdapter = LinkUrlAdapter(this)
        binding.rvLinks?.apply {
            adapter = recyclerLinksAdapter
            layoutManager = LinearLayoutManagerWrapper(requireContext())
        }
    }

    private fun updateFoldersList(folders: List<FolderObservable>) {
        val tabLayout = binding.tabLayoutFolder
        tabLayout.removeAllTabs()
        folders.forEach { observable -> tabLayout.addTab(tabLayout.createTab(observable)) }
        tabLayout.addTab(tabLayout.createAddFolderTab())
        tabLayoutListener?.also { listener -> tabLayout.removeOnTabSelectedListener(listener) }
        val listener = object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                if (tab.tag is Int && tab.tag == R.drawable.ic_baseline_add_black_24) {
                    openCreateFolderDialog()
                } else {
                    viewModel.listenLinksById(folders[tab.position].id)
                }
            }
        }
        tabLayout.addOnTabSelectedListener(listener)
        tabLayoutListener = listener
    }

    private fun TabLayout.createAddFolderTab(): TabLayout.Tab {
        val tab = newTab()
        val addFolderView = ImageView(context)
        addFolderView.setImageResource(R.drawable.ic_baseline_add_black_24)
        tab.tag = R.drawable.ic_baseline_add_black_24
        tab.customView = addFolderView
        return tab
    }

    private fun TabLayout.createTab(observable: FolderObservable): TabLayout.Tab {
        val tab = newTab()
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.tab_folder, this, false)
        view.tabFolder.text = observable.name
        view.setOnLongClickListener { _view -> showFolderSetting(_view, observable); false }
        view.setOnClickListener { tab.select() }
        tab.customView = view
        return tab
    }

    override fun onShareListener(link: BaseLinkObservable) {
        startActivity(AndroidUtils.createShareIntent(link.url, link.title))
    }

    override fun onItemListener(link: BaseLinkObservable) {
        if (viewModel.getEditModeState()) {
            viewModel.onEditLinkItem(link)
        } else {
            startActivity(AndroidUtils.createIntent(link.url))
            viewModel.moveUrlLinkToTop(link)
        }
    }

    override fun onItemAppOpenListener(app: LinkAppObservable) {
        val launchIntent = context?.packageManager?.getLaunchIntentForPackage(app.appId) ?: return
        launchIntent.data = Uri.parse(app.url)
        startActivity(launchIntent)
    }

    override fun onItemLongClickListener(link: BaseLinkObservable) {
        viewModel.onEditLinkItem(link)
    }

    override fun onAdClosed(adObservable: AdObservable) {
        viewModel.closeAdItem(adObservable)
    }

    private fun showAcceptDeleteAction() {
        val count = viewModel.getCheckedLinksCount()
        AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog)
            .setTitle(resources.getQuantityString(R.plurals.dialog_delete_link_title, count))
            .setMessage(resources.getQuantityString(R.plurals.dialog_delete_link_description, count, count))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.deleteUrlLinks()
                dialog?.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    private fun showFolderChoiceForAssignLinks() {
        val folders = viewModel.getFolderObservableList()
        val names = folders.map { observable -> observable.name }.toTypedArray()
        var selectedFolder: FolderObservable = folders.firstOrNull() ?: return
        AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog)
            .setTitle(R.string.assign_to_folder)
            .setSingleChoiceItems(names, ZERO) { _, which -> selectedFolder = folders[which] }
            .setPositiveButton(R.string.accept) { dialog, _ ->
                viewModel.assignLinksToFolder(selectedFolder.id)
                dialog?.dismiss()
            }
            .setNegativeButton(R.string.cancel) {  dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    private fun showFolderSetting(tabView: View, observable: FolderObservable) {
        if (observable.id == FolderObservable.DEF_FOLDER_ID) {
            return
        }

        val tabPosition = intArrayOf(ZERO, ZERO)
        tabView.getLocationOnScreen(tabPosition)
        val (left, top) = tabPosition
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_folder_setting, null, false)
        val dialog = AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog_Small)
            .setView(dialogView)
            .create()
        dialog.window?.apply {
            attributes.x = left
            attributes.y = top - getStatusBarHeight()
            attributes.gravity = Gravity.TOP or Gravity.START
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialogView.imageCard.tabFolder.apply {
            isSelected = true
            text = observable.name
        }
        dialogView.tvFolderRename.setOnClickListener {
            val bundle = bundleOf(FolderRenameDialogFragment.OBSERVABLE to observable)
            findNavController().navigate(R.id.action_mainFragment_to_folderRenameDialogFragment, bundle)
            dialog.dismiss()
        }
        dialogView.tvFolderDelete.setOnClickListener {
            openDeleteFolderDialog(observable)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier(IDENTIFIER_NAME, IDENTIFIER_DEF_TYPE, IDENTIFIER_DEF_PACKAGE)
        return if (resourceId > ZERO) resources.getDimensionPixelSize(resourceId) else ZERO
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


    private fun observeDeleteAcceptState() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        val liveData = savedStateHandle?.getLiveData<FolderObservable>(Config.KEY_ACCEPT_DELETE)
        liveData?.observe(viewLifecycleOwner, { folderObservable ->
            viewModel.deleteFolder(folderObservable.id)
        })
    }

    private fun openCreateClipboardDialog(url: String) {
        val bundle = Bundle().apply { putString(Config.CLIP_URL_LABEL, url) }
        findNavController().navigate(R.id.action_mainF_to_clipboardUrlDF, bundle)
    }

    private fun openDeleteFolderDialog(observable: FolderObservable) {
        val bundle = bundleOf(FolderDeleteDialogFragment.FOLDER_DATA to observable)
        findNavController().navigate(R.id.action_mainFragment_to_deleteFolder, bundle)
    }

    private fun openCreateFolderDialog() {
        findNavController().navigate(R.id.folderCreateDialogFragment)
    }

    private fun openProfileDialog() {
        findNavController().navigate(R.id.action_mainFragment_to_profileDialogFragment)
    }

    companion object {
        private const val ZERO = 0
        private const val IDENTIFIER_NAME = "status_bar_height"
        private const val IDENTIFIER_DEF_TYPE = "dimen"
        private const val IDENTIFIER_DEF_PACKAGE = "android"
    }
}