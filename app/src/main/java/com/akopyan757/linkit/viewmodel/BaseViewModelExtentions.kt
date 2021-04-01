package com.akopyan757.linkit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.model.entity.DataChange
import kotlinx.coroutines.launch

fun <T, V: BaseViewModel> LiveData<List<T>>.handleLiveList(
    viewModel: V,
    onSuccess: (List<T>) -> Unit
) = map { folders ->
    viewModel.viewModelScope.launch {
        onSuccess.invoke(folders)
    }
    Unit
}