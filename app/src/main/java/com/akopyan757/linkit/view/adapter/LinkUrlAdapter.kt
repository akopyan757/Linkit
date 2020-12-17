package com.akopyan757.linkit.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.view.callback.ItemTouchHelperAdapter
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import java.util.*

class LinkUrlAdapter(
    private val listener: LinkAdapterListener
): UpdatableListAdapter<LinkObservable>(), ItemTouchHelperAdapter {

    private var editMode: Boolean = false

    fun setEditMode(mode: Boolean) {
        editMode = mode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutId = R.layout.item_link
        val binding = DataBindingUtil.inflate<ItemLinkBinding>(inflater, layoutId, parent, false)
        return LinkViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LinkViewHolder).bind(items[position], editMode)
    }

    class LinkViewHolder(
        private val binding: ItemLinkBinding,
        private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(observable: LinkObservable, editMode: Boolean) {
            val context = binding.root.context
            val uri = AndroidUtils.getUriFromCache(context, observable.photoFileName)

            binding.observable = observable
            binding.editMode = editMode
            binding.ivLinkPhoto.setImageURI(uri)
            binding.listener = listener
            val colorRes = if (observable.selected) R.color.background else R.color.white
            binding.clLinkContent.setBackgroundResource(colorRes)
            binding.ivLinkDrag.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    listener.onStartDrag(this)
                }
                false
            }
            val marginRes = if (editMode) R.dimen.itemGuidelineBeginEdit else R.dimen.itemGuidelineBegin
            val margin = context.resources.getDimensionPixelSize(marginRes)
            binding.guidelineLink.setGuidelineBegin(margin)
            binding.executePendingBindings()
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
}