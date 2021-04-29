package com.akopyan757.linkit.view.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.databinding.ItemLinkSummaryCollapsedBinding
import com.akopyan757.linkit.databinding.ItemLinkSummaryLargeBinding
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkLargeObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable


class LinkUrlAdapter(
    private val listener: LinkAdapterListener
): UpdatableListAdapter<DiffItemObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_link -> {
                val binding = DataBindingUtil.inflate<ItemLinkBinding>(
                    inflater,
                    viewType,
                    parent,
                    false
                )
                LinkViewHolder(binding, listener)
            }
            R.layout.item_link_summary_large -> {
                val fullBinding = DataBindingUtil.inflate<ItemLinkSummaryLargeBinding>(
                    inflater,
                    viewType,
                    parent,
                    false
                )
                val collapsedBinding = DataBindingUtil.inflate<ItemLinkSummaryCollapsedBinding>(
                    inflater,
                    R.layout.item_link_summary_collapsed,
                    parent,
                    false
                )
                LinkLargeViewHolder(fullBinding, collapsedBinding, listener)
            }
            else -> {
                val view = inflater.inflate(viewType, parent, false)
                AdViewHolder(view, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> holder.bind(items[position] as LinkObservable)
            is LinkLargeViewHolder -> holder.bind(items[position] as LinkLargeObservable)
            is AdViewHolder -> holder.bind(items[position] as AdObservable)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is LinkObservable -> R.layout.item_link
            items[position] is LinkLargeObservable -> R.layout.item_link_summary_large
            else -> R.layout.layout_item_banner_ads
        }
    }

    class LinkViewHolder(
        private val binding: ItemLinkBinding,
        private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable) {
            binding.observable = observable
            binding.listener = listener
            binding.checked = observable.checked
            binding.executePendingBindings()
        }
    }

    class LinkLargeViewHolder(
        private val fullBinding: ItemLinkSummaryLargeBinding,
        private val collapsedBinding: ItemLinkSummaryCollapsedBinding,
        private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(fullBinding.root) {

        private val expandedSet: ConstraintSet = ConstraintSet()
        private val collapsedSet: ConstraintSet = ConstraintSet()

        init {
            expandedSet.clone(fullBinding.clLinkContent)
            collapsedSet.clone(collapsedBinding.clLinkContent)
        }

        fun bind(observable: LinkLargeObservable) {
            val autoTransition = AutoTransition().apply { duration = 600 }
            TransitionManager.beginDelayedTransition(fullBinding.clLinkContent, autoTransition)
            val drawable = observable.app?.let { appObservable ->
                fullBinding.root.context.packageManager.getApplicationIcon(appObservable.appId)
            }
            if (observable.isCollapsed()) {
                collapsedBinding.observable = observable
                collapsedBinding.listener = listener
                collapsedBinding.checked = observable.checked
                collapsedSet.applyTo(fullBinding.clLinkContent)
                collapsedBinding.ivLinkApp.setImageDrawable(drawable)
                collapsedBinding.executePendingBindings()
                fullBinding.ivLinkExpand.setImageResource(R.drawable.ic_arrow_down)
            } else {
                fullBinding.ivLinkExpand.setImageResource(R.drawable.ic_arrow_up)
                expandedSet.applyTo(fullBinding.clLinkContent)
            }
            fullBinding.observable = observable
            fullBinding.listener = listener
            fullBinding.checked = observable.checked
            fullBinding.ivLinkApp.setImageDrawable(drawable)
            fullBinding.ivLinkApp.visibility = if (drawable != null) View.VISIBLE else View.GONE
            fullBinding.executePendingBindings()
        }
    }

    class AdViewHolder(
        private val view: View,
        private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(view) {

        fun bind(observable: AdObservable) {
            val viewId = BannerViewExtension.getItemBannerViewId()
            BannerViewExtension.loadAd(view.findViewById(viewId), onClosed = {
                listener.onAdClosed(observable)
            })
        }
    }
}