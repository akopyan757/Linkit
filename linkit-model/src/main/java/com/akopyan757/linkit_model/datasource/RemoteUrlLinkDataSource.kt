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
    private val reference: CollectionReference,
    private val firebaseAuth: FirebaseAuth
): IRemoteUrlDataSource {

    companion object {
        private const val URLS = "urls"
        private const val ORDER = "order"
    }

    override fun loadUrlLinks() = Single.fromObservable<List<UrlLinkEntity>> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS).get()
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener { query ->
                    val urlLinks = query.documents.mapNotNull { urlLinkDocument ->
                        urlLinkDocument.toObject(UrlLinkEntity::class.java)
                    }
                    observer.onNext(urlLinks)
                    observer.onComplete()
                }
        }
    }

    override fun createOrUpdateUrlLink(data: UrlLinkEntity) = Observable.create<UrlLinkEntity> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .document(data.id).set(data)
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener {
                    observer.onNext(data)
                    observer.onComplete()
                }
        }
    }

    override fun deleteUrlLink(linkId: String) = Completable.fromObservable<Unit> { observer ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            observer.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .document(linkId)
                .delete()
                .addOnFailureListener(observer::onError)
                .addOnSuccessListener {
                    observer.onNext(Unit)
                    observer.onComplete()
                }
        }
    }

    override fun setOrderForUrlLink(linkId: String, order: Int) = Completable.create { source ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            source.onError(FirebaseUserNotFoundException())
        } else {
            val data = hashMapOf(ORDER to order)
            ref.collection(URLS)
                .document(linkId)
                .set(data, SetOptions.merge())
                .addOnFailureListener(source::onError)
                .addOnSuccessListener { source.onComplete() }
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
                        source.onComplete()
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

    private fun getUserDocumentOrNull(): DocumentReference? {
        return firebaseAuth.currentUser?.uid?.let { userUid ->
            reference.document(userUid)
        }
    }
}