package com.akopyan757.linkit.viewmodel.listener

import com.akopyan757.linkit.viewmodel.observable.*

interface LinkAdapterListener {
    fun onShareListener(link: BaseLinkObservable)
    fun onItemListener(link: BaseLinkObservable)
    fun onItemAppOpenListener(app: LinkAppObservable)
    fun onItemLongClickListener(link: BaseLinkObservable)
    fun onAdClosed(adObservable: AdObservable)
}