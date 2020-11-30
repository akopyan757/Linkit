package com.akopyan757.base.viewmodel

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

interface BaseStateObservable: Observable {

    fun getCallback(): PropertyChangeRegistry

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        getCallback().add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        getCallback().remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        getCallback().notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        getCallback().notifyCallbacks(this, fieldId, null)
    }

    /**
     * Notifies listeners that a specific properties has changed.
     */
    fun notifyArrayPropertiesChanged(vararg fieldIds: Int) {
        fieldIds.toTypedArray().forEach { fieldId ->
            getCallback().notifyCallbacks(this, fieldId, null)
        }
    }
}