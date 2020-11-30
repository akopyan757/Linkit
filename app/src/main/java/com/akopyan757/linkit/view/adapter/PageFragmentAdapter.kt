package com.akopyan757.linkit.view.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akopyan757.linkit.view.fragment.PageFragment
import com.akopyan757.linkit.viewmodel.observable.FolderObservable

class PageFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private var items: List<FolderObservable> = emptyList()

    fun getItems() = items

    fun updateFolders(folders: List<FolderObservable>) {
        items = folders
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = PageFragment.newInstance(items[position])
}