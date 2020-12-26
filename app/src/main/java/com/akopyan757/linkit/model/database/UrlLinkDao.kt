package com.akopyan757.linkit.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData

@Dao
interface UrlLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewData(data: UrlLinkData): Long {
        data._order = getMaxOrder() + 1
        return insertOrUpdate(data)
    }

    @Transaction
    fun insertOrUpdate(list: List<UrlLinkData>): List<Long> {
        return list.map { data -> insertOrUpdate(data) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: UrlLinkData): Long

    fun getLiveUrls(folderId: Int): LiveData<List<UrlLinkData>> {
        return if (folderId == FolderData.GENERAL_FOLDER_ID) {
            getLiveAll()
        } else {
            getLiveFromFolder(folderId)
        }
    }

    @Query("SELECT * FROM url_link_data WHERE url LIKE '%' || :host || '%' ")
    fun getByHost(host: String): List<UrlLinkData>

    @Query("SELECT * FROM url_link_data ORDER BY _order DESC")
    fun getLiveAll(): LiveData<List<UrlLinkData>>

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId ORDER BY _order DESC")
    fun getByFolder(folderId: Int): List<UrlLinkData>

    @Query("SELECT * FROM url_link_data WHERE folder_id LIKE :folderId ORDER BY _order DESC")
    fun getLiveFromFolder(folderId: Int): LiveData<List<UrlLinkData>>

    @Query("SELECT _order FROM url_link_data WHERE id LIKE :id ORDER BY _order DESC")
    fun getOrderById(id: Long): Int

    @Transaction
    fun getOrderByIds(ids: List<Long>): List<Int> {
        return ids.map { id -> getOrderById(id) }
    }

    @Query("UPDATE url_link_data SET _order = :order WHERE id == :id")
    fun updateOrder(id: Long, order: Int)

    @Transaction
    fun updateOrders(ids: List<Long>): List<Pair<Long, Int>> {
        val orders = getOrderByIds(ids).sortedDescending()
        return ids.mapIndexed { index, id ->
            updateOrder(id, orders[index])
            Pair(id, orders[index])
        }
    }

    @Query("DELETE from url_link_data WHERE id = :id")
    fun removeById(id: Long)

    @Transaction
    fun removeByIds(ids: List<Long>) {
        return ids.forEach { id -> removeById(id) }
    }

    @Query("DELETE from url_link_data WHERE folder_id LIKE :folderId")
    fun removeByFolder(folderId: String)

    @Query("DELETE FROM url_link_data")
    fun removeAll()

    @Query("SELECT MAX(_order) FROM url_link_data;")
    fun getMaxOrder(): Int
}