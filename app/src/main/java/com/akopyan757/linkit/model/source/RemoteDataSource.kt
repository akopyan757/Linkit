package com.akopyan757.linkit.model.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.DataChange
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FirebaseUserNotFound

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RemoteDataSource: KoinComponent {

    private val reference: CollectionReference by inject(named(Config.LINKS))
    private val database: FirebaseFirestore by inject()
    private val firebaseAuth: FirebaseAuth by inject()

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

    suspend fun loadUrlLinks(): List<UrlLinkData> = suspendCoroutine { cont ->
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

    suspend fun createOrUpdateFolder(data: FolderData) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.FOLDERS)
            .document(data.id)
            .set(data).addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteFolder(folderId: String) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.FOLDERS)
            .document(folderId)
            .delete().addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun createOrUpdateUrlLink(data: UrlLinkData) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.URLS)
            .document(data.id)
            .set(data).addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun deleteUrlLink(linkId: String) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        ref.collection(Config.URLS)
            .document(linkId)
            .delete()
            .addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun setNameForFolder(folderId: String, name: String) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        val newData = hashMapOf("name" to name)
        ref.collection(Config.FOLDERS)
            .document(folderId)
            .set(newData, SetOptions.merge())
            .addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun setOrderForUrlLink(linkId: String, order: Int) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont) ?: return@suspendCoroutine
        val newData = hashMapOf("_order" to order)
        ref.collection(Config.URLS)
            .document(linkId)
            .set(newData, SetOptions.merge())
            .addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun reorderFolders(sortedFolderIds: List<String>) = suspendCoroutine<Unit> { cont ->
        val ref = getUserDocument(cont)?.collection(Config.FOLDERS) ?: return@suspendCoroutine
        database.runBatch { writeBatch ->
            sortedFolderIds.forEachIndexed { index, folderId ->
                val folderDocs = ref.document(folderId)
                writeBatch.update(folderDocs, "order", index.plus(1))
            }
        }.addOnSuccessListener {
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            cont.resumeWithException(exception)
        }
    }

    fun listenFolderChanges(): LiveData<DataChange<FolderData>> {
        val dataChangeLiveData = MutableLiveData<DataChange<FolderData>>()
        getUserDocument<FolderData>()
            ?.collection(Config.FOLDERS)
            ?.addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    val exception = error ?: Exception("Url link snapshot exception")
                    dataChangeLiveData.postValue(DataChange.Error(exception))
                    return@addSnapshotListener
                }
                for (dc in snapshots.documentChanges) {
                    val folderData = dc.document.toObject(FolderData::class.java)
                    val dataChange = when (dc.type) {
                        DocumentChange.Type.ADDED -> DataChange.Added(folderData)
                        DocumentChange.Type.MODIFIED ->  DataChange.Modified(folderData)
                        DocumentChange.Type.REMOVED ->  DataChange.Deleted(folderData)
                    }
                    dataChangeLiveData.postValue(dataChange)
                }
            }
        return dataChangeLiveData
    }

    fun listenUrlLinksChanges(): LiveData<DataChange<UrlLinkData>> {
        val urlChangeLiveData = MutableLiveData<DataChange<UrlLinkData>>()
        getUserDocument<UrlLinkData>()
            ?.collection(Config.URLS)
            ?.addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    val exception = error ?: Exception("Url link snapshot exception")
                    urlChangeLiveData.postValue(DataChange.Error(exception))
                    return@addSnapshotListener
                }
                for (dc in snapshots.documentChanges) {
                    val urlLinkData = dc.document.toObject(UrlLinkData::class.java)
                    val dataChange = when (dc.type) {
                        DocumentChange.Type.ADDED -> DataChange.Added(urlLinkData)
                        DocumentChange.Type.MODIFIED ->  DataChange.Modified(urlLinkData)
                        DocumentChange.Type.REMOVED ->  DataChange.Deleted(urlLinkData)
                    }
                    urlChangeLiveData.postValue(dataChange)
                }
            }
        return urlChangeLiveData
    }

    private fun <T> getUserDocument(continuation: Continuation<T>? = null): DocumentReference? {
        val userUid = firebaseAuth.currentUser?.uid
        return if (userUid == null) {
            continuation?.resumeWithException(FirebaseUserNotFound())
            null
        } else {
            reference.document(userUid)
        }
    }
}