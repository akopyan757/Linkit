package com.akopyan757.linkit.viewmodel.observable

import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.linkit_domain.entity.FolderEntity
import java.io.Serializable

data class FolderObservable(
    val id: String,
    var name: String,
    val order: Int
): Serializable, DiffItemObservable {
    override fun id() = id

    companion object {
        fun getDefault(name: String) = FolderObservable(DEF_FOLDER_ID, name, DEF_FOLDER_ORDER)
        fun fromData(data: FolderEntity) = FolderObservable(data.id, data.name, data.order)

        const val DEF_FOLDER_ID = "-"
        private const val DEF_FOLDER_ORDER = 0
    }
}