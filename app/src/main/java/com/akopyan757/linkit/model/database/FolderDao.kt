package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.FolderData

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: FolderData): Long

    @Query("SELECT MAX(`order`) FROM folder_data;")
    fun getMaxOrder(): Int

    @Query("SELECT * FROM folder_data")
    fun getAllLive(): LiveData<List<FolderData>>

    @Query("SELECT * FROM folder_data")
    fun getAll(): List<FolderData>

    @Query("SELECT * FROM folder_data WHERE id = :id LIMIT 1")
    fun getById(id: String): FolderData?

    @Query("SELECT * FROM folder_data WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): FolderData?

    @Query("DELETE FROM folder_data WHERE id = :folderId")
    fun removeById(folderId: String)

    @Query("DELETE FROM folder_data")
    fun removeAll()
}