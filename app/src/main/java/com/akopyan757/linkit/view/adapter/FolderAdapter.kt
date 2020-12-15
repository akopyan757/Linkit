package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ItemFolderBinding
import com.akopyan757.linkit.viewmodel.listener.FolderClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable

class FolderAdapter(
        private val callback: FolderClickListener
): UpdatableListAdapter<FolderObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFolderBinding>(
                inflater, R.layout.item_folder, parent, false
        )
        return FolderViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? FolderViewHolder)?.bind(items[position])
    }

    class FolderViewHolder(
            private val binding: ItemFolderBinding,
            private val callback: FolderClickListener
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(observable: FolderObservable) {
            binding.observable = observable
            binding.listener = callback
            binding.executePendingBindings()
        }
    }
}