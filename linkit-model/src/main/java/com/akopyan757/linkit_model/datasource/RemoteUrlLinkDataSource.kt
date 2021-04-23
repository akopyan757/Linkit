package com.akopyan757.linkit_model.datasource

import android.util.Log
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
        Log.i("RemoteUrlLinkDataSource", "createOrUpdateUrlLink = $data")
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

    override fun deleteUrlLink(linkId: String) = Completable.create { emitter ->
        val ref = getUserDocumentOrNull()
        if (ref == null) {
            emitter.onError(FirebaseUserNotFoundException())
        } else {
            ref.collection(URLS)
                .document(linkId)
                .delete()
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

    private fun getUserDocumentOrNull(): DocumentReference? {
        return firebaseAuth.currentUser?.uid?.let { userUid ->
            reference.document(userUid)
        }
    }
}