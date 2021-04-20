package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_model.throwable.FirebaseUserNotFoundException
import com.akopyan757.linkit_model.throwable.ListenChangesException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RemoteFolderDataSource(
    private val reference: CollectionReference,
    private val database: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): IRemoteFolderDataSource {

    companion object {
        private const val FOLDERS = "folders"
        private const val ORDER = "order"
        private const val NAME = "name"
    }

    override fun loadFolders() = Single.create<List<FolderEntity>> { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(FOLDERS).get()
                .addOnFailureListener(emitter::onError)
                .addOnSuccessListener { query ->
                    val folders = query.documents.mapNotNull { folderDocument ->
                        folderDocument.toObject(FolderEntity::class.java)
                    }
                    emitter.onSuccess(folders)
                }
        }
    }

    override fun createOrUpdateFolder(data: FolderEntity) = Completable.fromObservable<Unit> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(FOLDERS)
                .document(data.id).set(data)
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener {
                    observer.onNext(Unit)
                    observer.onComplete()
                }
        }
    }

    override fun deleteFolder(folderId: String) = Completable.fromObservable<Unit> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(FOLDERS)
                .document(folderId).delete()
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener {
                    observer.onNext(Unit)
                    observer.onComplete()
                }
        }
    }

    override fun setFolderName(folderId: String, folderName: String) = Completable.fromObservable<Unit> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            val newData = hashMapOf(NAME to folderName)
            ref.collection(FOLDERS)
                .document(folderId)
                .set(newData, SetOptions.merge())
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener {
                    observer.onNext(Unit)
                    observer.onComplete()
                }
        }
    }

    override fun reorderFolders(folderIds: List<String>) = Completable.create { source ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            source.onError(FirebaseUserNotFoundException())
        } else {
            val collection = ref.collection(FOLDERS)
            database.runBatch { writeBatch ->
                folderIds.forEachIndexed { index, folderId ->
                    val folderDocs = collection.document(folderId)
                    writeBatch.update(folderDocs, ORDER, index.plus(1))
                }
            }
            .addOnFailureListener(source::onError)
            .addOnSuccessListener { source.onComplete() }
        }
    }

    override fun listenFolderChanges() = Observable.create<DataChange<FolderEntity>> { source ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            source.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(FOLDERS)
                .addSnapshotListener { snapshots, error ->
                    if (error != null || snapshots == null) {
                        val exception = error ?: ListenChangesException()
                        source.onError(exception)
                        return@addSnapshotListener
                    }
                    for (dc in snapshots.documentChanges) {
                        val folderData = dc.document.toObject(FolderEntity::class.java)
                        val dataChange = when (dc.type) {
                            DocumentChange.Type.ADDED -> DataChange.Added(folderData)
                            DocumentChange.Type.MODIFIED -> DataChange.Modified(folderData)
                            DocumentChange.Type.REMOVED -> DataChange.Deleted(folderData)
                        }
                        source.onNext(dataChange)
                    }
                }
        }
    }

    private fun getUserDocumentOrNull(): DocumentReference? {
        return firebaseAuth.currentUser?.uid?.let { userUid ->
            reference.document(userUid)
        }
    }
}