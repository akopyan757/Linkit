package com.akopyan757.linkit.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ItemFolderBinding
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.viewmodel.listener.FolderClickListener
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import java.util.*

class FolderAdapter(
        private val callback: FolderClickListener
): UpdatableListAdapter<FolderObservable>(), ItemTouchHelperAdapter {

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

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        swapItemsByPositions(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    private fun swapItemsByPositions(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            (fromPosition until toPosition).forEach { index ->
                Collections.swap(items, index, index + 1)
            }
        } else {
            (fromPosition downTo toPosition + 1).forEach { index ->
                Collections.swap(items, index, index - 1)
            }
        }
    }

    class FolderViewHolder(
            private val binding: ItemFolderBinding,
            private val callback: FolderClickListener
    ): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(observable: FolderObservable) {
            binding.observable = observable
            binding.listener = callback
            binding.ivFolderDrag.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN)
                    callback.onStartDrag(this)
                false
            }
            binding.executePendingBindings()
        }
    }
}