package com.akopyan757.linkit.viewmodel.listener

import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.viewmodel.observable.LinkObservable

interface LinkAdapterListener {
    fun onShareListener(link: LinkObservable, editMode: Boolean)
    fun onItemListener(link: LinkObservable, editMode: Boolean)
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}