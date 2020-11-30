package com.akopyan757.linkit.viewmodel.observable

import android.net.Uri
import com.akopyan757.base.viewmodel.DiffItemObservable

data class StoreObservable(
    val uri: Uri,
    val title: String,
    val size: String,
    val path: String?
): DiffItemObservable