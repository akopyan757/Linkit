package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.R
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.databinding.ItemLinkBoxBinding
import com.akopyan757.linkit.viewmodel.observable.LinkObservable

class LinkUrlAdapter(private val type: Type): UpdatableListAdapter<LinkObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val layoutId = when (type) {
            Type.ITEM ->  R.layout.item_link
            else -> R.layout.item_link_box
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LinkViewHolder).bind(items[position])
    }

    class LinkViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable) {
            when (binding) {
                is ItemLinkBinding -> {
                    binding.observable = observable
                    binding.executePendingBindings()
                }

                is ItemLinkBoxBinding -> {
                    binding.observable = observable
                    binding.executePendingBindings()
                }
            }
        }
    }

    enum class Type {
        ITEM, BOX
    }
}