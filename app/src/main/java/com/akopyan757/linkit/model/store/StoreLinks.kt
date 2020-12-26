package com.akopyan757.linkit.model.store

import android.util.Log
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.exception.FirebaseUserNotFound
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoreLinks: KoinComponent {

    private val reference: CollectionReference by inject(named(Config.LINKS))

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    suspend fun loadFolders(): List<FolderData> = suspendCoroutine { cont ->
        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            cont.resumeWithException(FirebaseUserNotFound())
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection(Config.FOLDERS).get()
            .addOnSuccessListener { query ->
                val folders = query.documents.mapNotNull { it.toObject(FolderData::class.java) }
                cont.resume(folders)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun addData(data: FolderData) = suspendCoroutine<Unit> { cont ->

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

    companion object {
        const val TAG = "STORE_LINKS"
    }

}