package com.akopyan757.linkit.viewmodel.listener

import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.viewmodel.observable.FolderObservable

interface FolderClickListener {
    fun onDeleteFolder(observable: FolderObservable)
    fun onEditFolder(observable: FolderObservable)
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}