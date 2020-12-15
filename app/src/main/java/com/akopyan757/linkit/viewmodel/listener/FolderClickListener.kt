package com.akopyan757.linkit.viewmodel.listener

import com.akopyan757.linkit.viewmodel.observable.FolderObservable

interface FolderClickListener {
    fun onDeleteFolder(observable: FolderObservable)
    fun onEditFolder(observable: FolderObservable)
}