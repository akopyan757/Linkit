package com.akopyan757.linkit.view.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akopyan757.linkit.view.fragment.PageFragment
import com.akopyan757.linkit.viewmodel.observable.FolderObservable

class PageFragmentAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    private var items: List<FolderObservable> = emptyList()

    fun getItems() = items

    fun updateFolders(folders: List<FolderObservable>) {
        items = folders
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = PageFragment.newInstance(items[position])
}