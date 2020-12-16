package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.databinding.ItemLinkBoxBinding
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.viewmodel.listener.LinkClickListener
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import java.util.*

class LinkUrlAdapter(
    private val type: Type,
    private val listener: LinkClickListener
): UpdatableListAdapter<LinkObservable>(), ItemTouchHelperAdapter {

    private var editMode: Boolean = false

    fun setEditMode(mode: Boolean) {
        editMode = mode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val layoutId = when (type) {
            Type.ITEM -> R.layout.item_link
            else -> R.layout.item_link_box
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return LinkViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LinkViewHolder).bind(items[position], editMode)
    }

    class LinkViewHolder(
        private val binding: ViewDataBinding,
        private val listener: LinkClickListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable, editMode: Boolean) {
            val context = binding.root.context
            val uri = AndroidUtils.getUriFromCache(context, observable.photoFileName)
            when (binding) {
                is ItemLinkBinding -> {
                    binding.observable = observable
                    binding.editMode = editMode
                    binding.ivLinkPhoto.setImageURI(uri)
                    binding.listener = listener
                    binding.executePendingBindings()
                }

                is ItemLinkBoxBinding -> {
                    binding.observable = observable
                    binding.ivLinkPhoto.setImageURI(uri)
                    binding.listener = listener
                    binding.executePendingBindings()
                }
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            (fromPosition until toPosition).forEach { index ->
                Collections.swap(items, index, index + 1)
            }
        } else {
            (fromPosition downTo toPosition + 1).forEach { index ->
                Collections.swap(items, index, index - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    enum class Type {
        ITEM, BOX
    }
}