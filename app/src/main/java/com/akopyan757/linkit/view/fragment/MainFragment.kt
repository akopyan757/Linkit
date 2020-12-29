package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.list.ListChangeStrategy
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.clipboard.ClipboardUtils
import com.akopyan757.linkit.databinding.FragmentMainBinding
import com.akopyan757.linkit.view.adapter.PageFragmentAdapter
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.LinkViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment<FragmentMainBinding, LinkViewModel>(), ViewTreeObserver.OnWindowFocusChangeListener, KoinComponent {

    private val firebaseAuth: FirebaseAuth by mainInject()

    override val mViewModel: LinkViewModel by viewModel()

    private lateinit var mAdapter: PageFragmentAdapter

    override fun getLayoutId() = R.layout.fragment_main
    override fun getVariableId() = BR.viewModel

    override fun onSetupView(binding: FragmentMainBinding, bundle: Bundle?): Unit = with(binding) {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.title = Config.EMPTY
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
                    R.id.itemEditDelete -> mViewModel.deleteSelected()
                }; true
            }
        }

        firebaseAuth.currentUser?.photoUrl?.also { url ->
            mViewModel.setProfileUrl(url.toString())
        }

        ivFolderSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_folderFragment)
        }

        tvFolderCreate.setOnClickListener {
            findNavController().navigate(R.id.folderCreateDialogFragment)
        }

        ccvIconProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profileDialogFragment)
        }

        mAdapter = PageFragmentAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = mAdapter

        TabLayoutMediator(tabLayoutFolder, viewPager) { tab, position ->
            tab.customView = mAdapter.getTabView(tabLayoutFolder, position)
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
        bindAllFolders()

        getLivePattern().observe(viewLifecycleOwner, Observer {
            Log.i("Pattern", "pattern")
        })

        getFolderLiveList().observe(viewLifecycleOwner, { holder ->
            if (holder.strategy is ListChangeStrategy.CustomChanged) {
                mAdapter.updateFolders(holder.data)
                holder.strategy.after?.invoke()
            }
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

            R.id.itemEditFolder -> {
                mViewModel.enableEditMode()
                mBinding.toolbarEdit.menu.setGroupVisible(R.id.groupEditSave, false)
                true
            }

            else -> false
        }
    }


}