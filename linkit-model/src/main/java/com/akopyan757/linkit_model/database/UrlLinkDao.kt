package com.akopyan757.linkit_model.database

import androidx.room.*
import com.akopyan757.linkit_model.database.data.UrlLinkData
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface UrlLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: UrlLinkData): Long

    @Query("SELECT EXISTS(SELECT * FROM url_link_data WHERE id = :linkId LIMIT 1)")
    fun checkExistLink(linkId: String): Boolean

    @Query("SELECT * FROM url_link_data ORDER BY _order DESC")
    fun getAll(): List<UrlLinkData>

    @Query("SELECT * FROM url_link_data ORDER BY _order DESC")
    fun getAllLive(): Flowable<List<UrlLinkData>>

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId ORDER BY _order DESC")
    fun getByFolderLive(folderId: String): Flowable<List<UrlLinkData>>

    @Query("DELETE from url_link_data WHERE id = :id")
    fun removeById(id: String)

    @Query("SELECT MAX(_order) FROM url_link_data;")
    fun getMaxOrder(): Int

    @Query("DELETE FROM url_link_data;")
    fun removeAll()

    @Transaction
    fun updateAll(links: List<UrlLinkData>) {
        removeAll()
        links.forEach { link -> insertOrUpdate(link) }
    }
}