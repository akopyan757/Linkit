package com.akopyan757.linkit.model.store

import android.util.Log
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.FolderData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StoreLinks: KoinComponent {

    private val reference: CollectionReference by inject(named(Config.LINKS))

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    suspend fun addData(data: FolderData): ApiResponse<Unit> = suspendCoroutine { cont ->

        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            cont.resume(ApiResponse.Error(Exception("Firebase User not found")))
            return@suspendCoroutine
        }

        reference.document(uid)
            .collection("folders")
            .document(data.id.toString())
            .set(data).addOnSuccessListener {
                Log.i(TAG, "addData: success: uid=$uid, folder=${data.name}")
                cont.resume(ApiResponse.Success(Unit))
            }.addOnFailureListener { exception ->
                Log.e(TAG, "addData: failure:", exception)
                cont.resume(ApiResponse.Error(exception))
            }
    }

    companion object {
        const val TAG = "STORE_LINKS"
    }

}