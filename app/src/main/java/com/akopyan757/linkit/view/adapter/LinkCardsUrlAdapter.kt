package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ContentItemLinkBinding
import com.akopyan757.linkit.viewmodel.observable.LinkObservable


class LinkCardsUrlAdapter: UpdatableListAdapter<LinkObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ContentItemLinkBinding>(
            inflater, R.layout.content_item_link, parent, false
        )
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LinkViewHolder) holder.bind(items[position])
    }

    class LinkViewHolder(
        private val binding: ContentItemLinkBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable) {
            binding.observable = observable
            binding.executePendingBindings()
        }
    }
}