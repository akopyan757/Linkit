package com.akopyan757.linkit_model.mapper

import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_model.database.data.FolderData

class FolderMapper: Mapper<FolderData, FolderEntity> {

    override fun firstToSecond(data: FolderData): FolderEntity {
        return FolderEntity(data.id, data.name, data.order)
    }

    override fun secondToFirst(data: FolderEntity): FolderData {
        return FolderData(data.id, data.name, data.order)
    }
}