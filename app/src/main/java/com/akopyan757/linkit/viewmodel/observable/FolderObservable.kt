package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class FolderObservable(
    val id: Int,
    var name: String
): Serializable, DiffItemObservable