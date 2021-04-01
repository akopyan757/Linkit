package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.model.repository.LinkRepository

import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent

class PreviewUrlViewModel(observable: LinkObservable) : BaseViewModel(), KoinComponent {

    @get:Bindable var toolbarTitle: String = observable.title
    @get:Bindable var previewUrl: String = observable.url
}