package com.akopyan757.linkit_model.database

import androidx.room.*
import com.akopyan757.linkit_model.database.data.UrlLinkData
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface UrlLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: UrlLinkData): Long

    @Query("UPDATE url_link_data SET _order = :order WHERE id == :linkId")
    fun updateLinkOrder(linkId: String, order: Int)

    @Query("UPDATE url_link_data SET folder_id = :folderId WHERE id = :linkId")
    fun updateLinkFolder(linkId: String, folderId: String)

    @Query("UPDATE url_link_data SET collapsed = :collapsed WHERE id = :linkId")
    fun updateLinkCollapse(linkId: String, collapsed: Boolean)

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
    fun updateAssignFolderForLinks(linkIds: List<String>, folderId: String) {
        linkIds.forEach { linkId -> updateLinkFolder(linkId, folderId) }
    }

    @Transaction
    fun removeByIds(ids: List<String>) {
        ids.forEach { id -> removeById(id) }
    }

    @Transaction
    fun updateAll(links: List<UrlLinkData>) {
        removeAll()
        links.forEach { link -> insertOrUpdate(link) }
    }
}