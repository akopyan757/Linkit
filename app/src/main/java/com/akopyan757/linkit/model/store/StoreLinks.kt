package com.akopyan757.linkit.model.store

import android.util.Log
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FirebaseUserNotFound
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoreLinks: KoinComponent {

    private val reference: CollectionReference by inject(named(Config.LINKS))

    private val database: FirebaseFirestore by inject()

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    suspend fun loadFolders(): List<FolderData> = suspendCoroutine { cont ->
        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.FOLDERS)
            .get()
            .addOnSuccessListener { query ->
                val folders = query.documents.mapNotNull { it.toObject(FolderData::class.java) }
                cont.resume(folders)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun loadUrls(): List<UrlLinkData> = suspendCoroutine { cont ->
        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.URLS)
            .get()
            .addOnSuccessListener { query ->
                val folders = query.documents.mapNotNull { it.toObject(UrlLinkData::class.java) }
                cont.resume(folders)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun addFolder(data: FolderData) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.FOLDERS)
            .document(data.id.toString())
            .set(data).addOnSuccessListener {
                Log.i(TAG, "addData: success: uid=$uid, folder=${data.name}")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "addData: failure:", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteFolder(folderId: Int) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.FOLDERS)
            .document(folderId.toString())
            .delete().addOnSuccessListener {
                Log.i(TAG, "deleteFolder: success: uid=$uid, folderId=$folderId")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "deleteFolder: failure:", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun addLink(data: UrlLinkData) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.URLS)
            .document(data.id.toString())
            .set(data).addOnSuccessListener {
                Log.i(TAG, "addData: success: uid=$uid, url=${data.url}")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "addData: failure:", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteUrls(urlIds: List<Long>) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        val ref = reference.document(uid).collection(Config.URLS)

        database.runBatch { writeBatch ->
            urlIds.forEach { id ->
                writeBatch.delete(ref.document(id.toString()))
            }
        }.addOnSuccessListener {
            Log.i(TAG, "deleteFolder: success: uid=$uid")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "deleteFolder: failure:", exception)
            cont.resumeWithException(exception)
        }
    }

    suspend fun reorderUrl(orders: List<Pair<Long, Int>>) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        val ref = reference.document(uid).collection(Config.URLS)

        database.runBatch { writeBatch ->
            orders.forEach { (id, order) ->
                Log.i(TAG, "reorderUrl: id=$id, order=$order")
                writeBatch.update(ref.document(id.toString()), "_order", order)
            }
        }.addOnSuccessListener {
            Log.i(TAG, "reorderUrl: success: uid=$uid")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "reorderUrl: failure:", exception)
            cont.resumeWithException(exception)
        }
    }


    suspend fun reorderFolders(orders: List<Pair<Int, Int>>) = suspendCoroutine<Unit> { cont ->

        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        val ref = reference.document(uid).collection(Config.FOLDERS)

        database.runBatch { writeBatch ->
            orders.forEach { (id, order) ->
                Log.i(TAG, "reorderUrl: id=$id, order=$order")
                writeBatch.update(ref.document(id.toString()), "order", order)
            }
        }.addOnSuccessListener {
            Log.i(TAG, "reorderUrl: success: uid=$uid")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "reorderUrl: failure:", exception)
            cont.resumeWithException(exception)
        }
    }

    companion object {
        const val TAG = "STORE_LINKS"
    }

}