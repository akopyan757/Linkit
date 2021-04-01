package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.UrlLinkData

@Dao
interface UrlLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: UrlLinkData): Long

    @Query("SELECT * FROM url_link_data WHERE id = :id LIMIT 1")
    fun getById(id: String): UrlLinkData?

    @Query("SELECT * FROM url_link_data ORDER BY _order DESC")
    fun getAllLive(): LiveData<List<UrlLinkData>>

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId ORDER BY _order DESC")
    fun getByFolderLive(folderId: String): LiveData<List<UrlLinkData>>

    @Query("DELETE from url_link_data WHERE id = :id")
    fun removeById(id: String)

    @Query("SELECT MAX(_order) FROM url_link_data;")
    fun getMaxOrder(): Int
}