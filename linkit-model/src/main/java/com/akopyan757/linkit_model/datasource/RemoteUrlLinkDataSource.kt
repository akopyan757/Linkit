package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_model.throwable.FirebaseUserNotFoundException
import com.akopyan757.linkit_model.throwable.ListenChangesException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RemoteUrlLinkDataSource(
    private val fireStore: FirebaseFirestore,
    private val reference: CollectionReference,
    private val firebaseAuth: FirebaseAuth
): IRemoteUrlDataSource {

    companion object {
        private const val URLS = "urls"
        private const val ORDER = "order"
        private const val FOLDER_ID = "folderId"
        private const val COLLAPSED = "collapsed"
    }

    override fun loadUrlLinks() = Single.create<List<UrlLinkEntity>> { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS).get()
                .addOnFailureListener(emitter::onError)
                .addOnSuccessListener { query ->
                    val urlLinks = query.documents.mapNotNull { urlLinkDocument ->
                        urlLinkDocument.toObject(UrlLinkEntity::class.java)
                    }
                    emitter.onSuccess(urlLinks)
                }
        }
    }

    override fun createOrUpdateUrlLink(data: UrlLinkEntity) = Single.create<UrlLinkEntity> { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .document(data.id)
                .set(data)
                .addOnFailureListener(emitter::onError)
                .addOnSuccessListener { emitter.onSuccess(data) }
        }
    }

    override fun deleteUrlLinks(linkIds: List<String>) = Completable.create { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            fireStore.runTransaction { transaction ->
                val collection = ref.collection(URLS)
                linkIds.forEach { linkId ->
                    val document = collection.document(linkId)
                    transaction.delete(document)
                }
            }
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { emitter.onComplete() }
        }
    }

    override fun setOrderForUrlLink(linkId: String, order: Int) = Single.create<Int> { source ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            source.onError(FirebaseUserNotFoundException())
        } else {
            val data = hashMapOf(ORDER to order)
            ref.collection(URLS)
                .document(linkId)
                .set(data, SetOptions.merge())
                .addOnFailureListener(source::onError)
                .addOnSuccessListener { source.onSuccess(order) }
        }
    }

    override fun listenUrlLinksChanges() = Observable.create<DataChange<UrlLinkEntity>> { source ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            source.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .addSnapshotListener { snapshots, error ->
                    if (error != null || snapshots == null) {
                        val exception = error ?: ListenChangesException()
                        source.onError(exception)
                        return@addSnapshotListener
                    }
                    for (dc in snapshots.documentChanges) {
                        val urlLinkData = dc.document.toObject(UrlLinkEntity::class.java)
                        val dataChange = when (dc.type) {
                            DocumentChange.Type.ADDED -> DataChange.Added(urlLinkData)
                            DocumentChange.Type.MODIFIED -> DataChange.Modified(urlLinkData)
                            DocumentChange.Type.REMOVED -> DataChange.Deleted(urlLinkData)
                        }
                        source.onNext(dataChange)
                    }
                }
        }
    }

    override fun changeUrlCollapse(linkId: String, collapse: Boolean) = Completable.create { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .document(linkId)
                .update(COLLAPSED, collapse)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { emitter.onComplete() }
        }
    }

    override fun assignLinksToFolder(folderId: String, links: List<String>) = Completable.create { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            fireStore.runTransaction { transition ->
                val collection = ref.collection(URLS)
                links.forEach { linkId ->
                    val document = collection.document(linkId)
                    transition.update(document, FOLDER_ID, folderId)
                }
            }
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { emitter.onComplete() }
        }
    }

    private fun getUserDocumentOrNull(): DocumentReference? {
        return firebaseAuth.currentUser?.uid?.let { userUid ->
            reference.document(userUid)
        }
    }
}