package com.akopyan757.linkit.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.linkit.BannerViewExtension
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.ItemLinkBinding
import com.akopyan757.linkit.viewmodel.listener.LinkAdapterListener
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable


class LinkUrlAdapter(
        private val listener: LinkAdapterListener
): UpdatableListAdapter<DiffItemObservable>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == R.layout.item_link) {
            val binding = DataBindingUtil.inflate<ItemLinkBinding>(inflater, viewType, parent, false)
            LinkViewHolder(binding, listener)
        } else {
            val view = inflater.inflate(viewType, parent, false)
            AdViewHolder(view, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> {
                val observable = getUpdatedObservable(items[position] as LinkObservable)
                if (observable != null) holder.bind(observable)
            }
            is AdViewHolder -> holder.bind(items[position] as AdObservable)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is LinkObservable) {
            R.layout.item_link
        } else {
            R.layout.layout_item_banner_ads
        }
    }

    private fun getUpdatedObservable(observable: LinkObservable): LinkObservable? {
        val context = view?.context ?: return null
        observable.uri = AndroidUtils.getUriFromCache(context, observable.photoFileName)
        observable.photoVisible = observable.uri != null
        return observable
    }

    class LinkViewHolder(
            private val binding: ItemLinkBinding,
            private val listener: LinkAdapterListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(observable: LinkObservable) {
            val newLinkObservable = getUpdatedObservable(observable)
            val backgroundColor = getLinkCardBackgroundColor(newLinkObservable.selected)
            binding.observable = newLinkObservable
            binding.listener = listener
            binding.mcvLinkContent.setCardBackgroundColor(backgroundColor)
            binding.executePendingBindings()
        }

        private fun getUpdatedObservable(observable: LinkObservable): LinkObservable {
            val context = binding.root.context
            observable.uri = AndroidUtils.getUriFromCache(context, observable.photoFileName)
            observable.photoVisible = observable.uri != null
            return observable
        }

        private fun getLinkCardBackgroundColor(selected: Boolean): Int {
            val colorRes = if (selected) R.color.greyLight else R.color.white
            val context = binding.root.context
            return ContextCompat.getColor(context, colorRes)
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