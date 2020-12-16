package com.akopyan757.linkit.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.view.fragment.PageFragment
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import kotlinx.android.synthetic.main.tab_folder.view.*

class PageFragmentAdapter(
        private val activity: FragmentActivity
): FragmentStateAdapter(activity) {

    private var items: List<FolderObservable> = emptyList()

    fun getItems() = items

    fun updateFolders(folders: List<FolderObservable>) {
        items = folders
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = PageFragment.newInstance(items[position])

    @SuppressLint("InflateParams")
    fun getTabView(position: Int): View {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.tab_folder, null, false)
        view.tabFolder.text = items[position].name
        return view
    }
}