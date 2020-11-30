package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.R
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.databinding.ItemAddUrlFolderBinding
import com.akopyan757.linkit.viewmodel.observable.FolderObservable

class LinkFoldersAdapter(
    private val onAddFolders: () -> Unit
): UpdatableListAdapter<FolderObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == R.layout.item_add_url_folder) {
            val binding = DataBindingUtil.inflate<ItemAddUrlFolderBinding>(
                inflater, viewType, parent, false
            )
            LinkFolderViewHolder(binding)
        } else {
            val view = inflater.inflate(viewType, parent, false)
            view.setOnClickListener {
                onAddFolders.invoke()
            }
            AddFoldersViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LinkFolderViewHolder) holder.bind(items[position])
    }

    override fun getItemCount() = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) R.layout.item_add_url_folder else R.layout.item_add_url_folder_add
    }

    class LinkFolderViewHolder(private val binding: ItemAddUrlFolderBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(folderObservable: FolderObservable) {
            binding.observable = folderObservable
            binding.executePendingBindings()
        }
    }

    class AddFoldersViewHolder(view: View): RecyclerView.ViewHolder(view)
}