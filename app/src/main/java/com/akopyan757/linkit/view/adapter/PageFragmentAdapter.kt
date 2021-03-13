package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.view.fragment.PageFragment
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import kotlinx.android.synthetic.main.tab_folder.view.*

class PageFragmentAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    private var folderItems: List<FolderObservable> = emptyList()

    fun updateFolders(folders: List<FolderObservable>) {
        folderItems = folders
        notifyDataSetChanged()
    }

    override fun getItemCount() = folderItems.size

    override fun createFragment(position: Int) = PageFragment.newInstance(folderItems[position])

    fun getTabView(parent: ViewGroup, position: Int): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tab_folder, parent, false)
        val tabName = folderItems[position].name
        view.tabFolder.text =  FormatUtils.updateStringFormat(tabName)
        return view
    }
}