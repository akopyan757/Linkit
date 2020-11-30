package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.R
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.databinding.ItemStoreBinding
import com.akopyan757.linkit.viewmodel.observable.StoreObservable

class LinkStoreAdapter: UpdatableListAdapter<StoreObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemStoreBinding>(inflater, R.layout.item_store, parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LinkViewHolder).bind(items[position])
    }

    class LinkViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: StoreObservable) {
            binding.observable = observable
            binding.executePendingBindings()
        }
    }
}