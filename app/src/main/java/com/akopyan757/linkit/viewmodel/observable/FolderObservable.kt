package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import java.io.Serializable

data class FolderObservable(
    val id: Int,
    var name: String,
    val type: Int
): Serializable, DiffItemObservable