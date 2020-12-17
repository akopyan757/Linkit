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

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId")
    fun getByFolder(folderId: Int): List<UrlLinkData>

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId")
    fun getLiveFromFolder(folderId: Int): LiveData<List<UrlLinkData>>

    @Query("UPDATE url_link_data SET _order = :order WHERE id == :id")
    fun updateOrder(id: Long, order: Int)

    @Query("DELETE from url_link_data WHERE id = :id")
    fun removeById(id: Long)

    @Query("DELETE from url_link_data WHERE folder_id LIKE :folderId")
    fun removeByFolder(folderId: String)

    @Query("DELETE FROM url_link_data")
    fun removeAll()
}