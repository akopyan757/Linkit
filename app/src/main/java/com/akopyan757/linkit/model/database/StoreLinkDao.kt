package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akopyan757.linkit.model.entity.StoreLinkData

@Dao
interface StoreLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: StoreLinkData)

    @Query("SELECT * FROM store_link_data")
    fun getLiveAll(): LiveData<List<StoreLinkData>>

    @Query("SELECT * FROM store_link_data WHERE folder_ids = :folderId")
    fun getLiveFromFolder(folderId: String): LiveData<List<StoreLinkData>>

    @Query("DELETE from store_link_data WHERE uri = :uri")
    fun removeByUri(uri: String)

    @Query("DELETE from store_link_data WHERE folder_ids = :folderId")
    fun removeByFolder(folderId: String)

    @Query("DELETE FROM store_link_data")
    fun removeAll()
}