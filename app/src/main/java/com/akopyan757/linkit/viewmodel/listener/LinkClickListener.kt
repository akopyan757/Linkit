package com.akopyan757.linkit.viewmodel.listener

import com.akopyan757.linkit.viewmodel.observable.LinkObservable

interface LinkClickListener {
    fun onShareListener(link: LinkObservable)
    fun onItemListener(link: LinkObservable)
}