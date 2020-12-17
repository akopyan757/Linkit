package com.akopyan757.linkit.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class SumLiveData: MediatorLiveData<Int>() {

    private var mapValues = HashMap<String, Int>()

    fun removeAll() {
        mapValues.clear()
    }

    fun addSource(liveData: LiveData<Int>, key: String) {
        addSource(liveData) { count ->
            mapValues[key] = count
            value = mapValues.values.sum()
        }
    }
}