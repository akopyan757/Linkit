package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.FolderData

@Dao
interface FolderDao {

    @Transaction
    fun addNewFolder(name: String): Boolean {
        val folder = getByName(name)
        if (folder != null) return false
        val order = getMaxOrder() + 1
        insertOrUpdate(FolderData(name=name, order=order))
        return true
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: FolderData)

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