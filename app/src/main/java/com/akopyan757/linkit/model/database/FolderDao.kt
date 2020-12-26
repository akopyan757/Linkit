package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.FolderData

@Dao
interface FolderDao {

    @Transaction
    fun initFolderDao(): Boolean {
        return if (getById(FolderData.GENERAL_FOLDER_ID) == null) {
            insertOrUpdate(FolderData.generalFolder()); true
        } else false
    }

    @Transaction
    fun addNewFolder(name: String): FolderData? {
        val folder = getByName(name)
        if (folder != null) return null
        val order = getMaxOrder() + 1
        val data = FolderData(name=name, order=order)
        val id = insertOrUpdate(data)
        data.id = id.toInt()
        return data
    }

    @Transaction
    fun insertOrUpdate(list: List<FolderData>) {
        list.forEach { data -> insertOrUpdate(data) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: FolderData): Long

    @Query("SELECT MAX(`order`) FROM folder_data;")
    fun getMaxOrder(): Int

    @Query("SELECT * FROM folder_data WHERE id = :id LIMIT 1")
    fun getById(id: Int): FolderData?

    @Query("SELECT * FROM folder_data WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): FolderData?

    @Query("SELECT * FROM folder_data ORDER BY `order`")
    fun getLiveAll(): LiveData<List<FolderData>>

    @Query("UPDATE folder_data SET name = :name WHERE id == :id")
    fun updateName(id: Int, name: String)

    @Query("UPDATE folder_data SET `order` = :order WHERE id == :id")
    fun updateOrder(id: Int, order: Int)

    @Transaction
    fun updateOrders(pairs: List<Pair<Int, Int>>) {
        pairs.forEach { (id, order) -> updateOrder(id, order) }
    }

    @Query("DELETE FROM folder_data WHERE id = :folderId")
    fun removeById(folderId: Int)

    @Query("DELETE FROM folder_data")
    fun removeAll()
}