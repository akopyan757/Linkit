package com.akopyan757.linkit_model.database

import androidx.room.*
import com.akopyan757.linkit_model.database.data.FolderData
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: FolderData): Long

    @Query("SELECT MAX(`order`) FROM folder_data;")
    fun getMaxOrder(): Int

    @Query("SELECT * FROM folder_data")
    fun getAllLive(): Flowable<List<FolderData>>

    @Query("SELECT * FROM folder_data")
    fun getAll(): List<FolderData>

    @Query("SELECT EXISTS (SELECT * FROM folder_data WHERE id = :id LIMIT 1)")
    fun checkFolderExist(id: String): Boolean

    @Query("SELECT * FROM folder_data WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): FolderData?

    @Query("DELETE FROM folder_data WHERE id = :folderId")
    fun removeById(folderId: String)

    @Query("DELETE FROM folder_data")
    fun removeAll()

    @Transaction
    fun updateAll(folders: List<FolderData>) {
        removeAll()
        folders.forEach { folder -> insertOrUpdate(folder) }
    }
}