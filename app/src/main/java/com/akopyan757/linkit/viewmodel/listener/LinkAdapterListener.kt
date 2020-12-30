package com.akopyan757.linkit.viewmodel.listener

import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable

interface LinkAdapterListener {
    fun onShareListener(link: LinkObservable)
    fun onItemListener(link: LinkObservable)
    fun onItemLongClickListener(link: LinkObservable)
    fun onAdClosed(adObservable: AdObservable)
}