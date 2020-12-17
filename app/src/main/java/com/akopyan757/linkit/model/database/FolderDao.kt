package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.FolderData

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: FolderData)

    @Query("SELECT * FROM folder_data WHERE id = :id LIMIT 1")
    fun getById(id: Int): FolderData?

    @Query("SELECT * FROM folder_data WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): FolderData?

    @Query("SELECT * FROM folder_data")
    fun getLiveAll(): LiveData<List<FolderData>>

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