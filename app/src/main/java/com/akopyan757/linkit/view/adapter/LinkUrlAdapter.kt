package com.akopyan757.linkit.view.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable


class LinkUrlAdapter(
    private val listener: LinkAdapterListener
): UpdatableListAdapter<DiffItemObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_link -> {
                val binding = DataBindingUtil.inflate<ItemLinkBinding>(
                    inflater, viewType, parent, false
                )
                LinkViewHolder(binding, listener)
            }

            else -> {
                AdViewHolder(inflater.inflate(viewType, parent, false), listener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> holder.bind(items[position] as LinkObservable)
            is AdViewHolder -> holder.bind(items[position] as AdObservable)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is LinkObservable -> R.layout.item_link
            items[position] is LinkObservable -> R.layout.item_link
            else -> R.layout.layout_item_banner_ads
        }
    }

    class LinkViewHolder(
        private val binding: ItemLinkBinding,
        private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable) {
            val autoTransition = AutoTransition().apply { duration = 600 }
            TransitionManager.beginDelayedTransition(binding.clLinkContent, autoTransition)
            val drawable = observable.app?.let { appObservable ->
                binding.root.context.packageManager.getApplicationIcon(appObservable.appId)
            }
            binding.observable = observable
            binding.listener = listener
            binding.checked = observable.checked
            binding.ivLinkApp.setImageDrawable(drawable)
            binding.ivLinkApp.visibility = if (drawable != null) View.VISIBLE else View.GONE
            binding.executePendingBindings()
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