package com.akopyan757.linkit.model.store

import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FirebaseUserNotFound
import com.akopyan757.linkit.view.scope.mainInject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.KoinComponent
import org.koin.core.qualifier.named
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoreLinks: KoinComponent {

    private val reference: CollectionReference by mainInject(named(Config.LINKS))
    private val database: FirebaseFirestore by mainInject()
    private val firebaseAuth: FirebaseAuth by mainInject()

    suspend fun loadFolders(): List<FolderData> = suspendCoroutine { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.FOLDERS)
            .get()
            .addOnSuccessListener { query ->
                val folders = query.documents.mapNotNull { it.toObject(FolderData::class.java) }
                cont.resume(folders)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun loadUrls(): List<UrlLinkData> = suspendCoroutine { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.URLS)
            .get()
            .addOnSuccessListener { query ->
                val folders = query.documents.mapNotNull { it.toObject(UrlLinkData::class.java) }
                cont.resume(folders)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun addFolder(data: FolderData) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.FOLDERS)
            .document(data.id.toString())
            .set(data).addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteFolder(folderId: Int) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.FOLDERS)
            .document(folderId.toString())
            .delete().addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun addLink(data: UrlLinkData) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.URLS)
            .document(data.id.toString())
            .set(data).addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteUrls(urlIds: List<Long>) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont)?.collection(Config.URLS) ?: return@suspendCoroutine
        database.runBatch { writeBatch ->
            urlIds.forEach { id ->
                writeBatch.delete(ref.document(id.toString()))
            }
        }.addOnSuccessListener {
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            cont.resumeWithException(exception)
        }
    }

    suspend fun reorderUrl(orders: List<Pair<Long, Int>>) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont)?.collection(Config.URLS) ?: return@suspendCoroutine
        database.runBatch { writeBatch ->
            orders.forEach { (id, order) ->
                writeBatch.update(ref.document(id.toString()), "_order", order)
            }
        }.addOnSuccessListener {
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            cont.resumeWithException(exception)
        }
    }

    suspend fun reorderFolders(orders: List<Pair<Int, Int>>) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont)?.collection(Config.FOLDERS) ?: return@suspendCoroutine
        database.runBatch { writeBatch ->
            orders.forEach { (id, order) ->
                writeBatch.update(ref.document(id.toString()), "order", order)
            }
        }.addOnSuccessListener {
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            cont.resumeWithException(exception)
        }
    }

    private fun <T> getUserDocument(continuation: Continuation<T>): DocumentReference? {
        val userUid = firebaseAuth.currentUser?.uid
        return if (userUid == null) {
            continuation.resumeWithException(FirebaseUserNotFound())
            null
        } else {
            reference.document(userUid)
        }
    }
}