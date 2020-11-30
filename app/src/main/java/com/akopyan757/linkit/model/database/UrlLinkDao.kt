package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akopyan757.linkit.model.entity.UrlLinkData

@Dao
interface UrlLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: UrlLinkData)

    @Query("SELECT * FROM url_link_data")
    fun getLiveAll(): LiveData<List<UrlLinkData>>

    @Query("SELECT * FROM url_link_data WHERE folder_ids LIKE :folderId")
    fun getLiveFromFolder(folderId: String): LiveData<List<UrlLinkData>>

    @Query("DELETE from url_link_data WHERE url = :uri")
    fun removeByUri(uri: String)

    @Query("DELETE from url_link_data WHERE folder_ids LIKE :folderId")
    fun removeByFolder(folderId: String)

    @Query("DELETE FROM url_link_data")
    fun removeAll()
}