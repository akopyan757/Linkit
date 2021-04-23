package com.akopyan757.linkit.viewmodel.listener

import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.BaseLinkObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable

interface LinkAdapterListener {
    fun onShareListener(link: BaseLinkObservable)
    fun onItemListener(link: BaseLinkObservable)
    fun onItemLongClickListener(link: BaseLinkObservable)
    fun onDeleteListener(link: BaseLinkObservable)
    fun onEditListener(link: BaseLinkObservable)
    fun onAdClosed(adObservable: AdObservable)
}