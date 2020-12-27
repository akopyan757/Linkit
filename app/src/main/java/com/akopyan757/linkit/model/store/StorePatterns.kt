package com.akopyan757.linkit.model.store

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.PatternHostData
import com.akopyan757.linkit.view.scope.mainInject
import com.google.firebase.database.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class StorePatterns: KoinComponent {

    companion object {
        private const val TAG = "STORE_PATTERNS"
    }

    private val reference: DatabaseReference by mainInject(named(Config.PATTERNS))

    private val patterns = mutableListOf<PatternHostData>()

    private val livePatterns = MutableLiveData<List<PatternHostData>>()
    private val liveErrors = MutableLiveData<Exception>()

    fun getLiveError(): LiveData<Exception> = liveErrors
    fun getLivePatterns(): LiveData<List<PatternHostData>> = livePatterns

    fun removeObserveItem(patternHostData: PatternHostData) {
        patterns.removeAll { it.host == patternHostData.host }
    }

    fun fetchData() {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                try {
                    snapshot.getValue(PatternHostData::class.java)?.also { data ->
                        patterns.add(data)
                        Log.i(TAG, "patterns.size=${patterns.size} data=${data}")
                        livePatterns.value = patterns
                    }
                } catch (e: DatabaseException) {}
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                try {
                    snapshot.getValue(PatternHostData::class.java)?.also { data ->
                        patterns.add(data)
                        Log.i(TAG, "patterns.size=${patterns.size} data=${data}")
                        livePatterns.value = patterns
                    }
                } catch (e: DatabaseException) {}
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                try {
                    val data = snapshot.getValue(PatternHostData::class.java)
                    Log.i(TAG, "onChildRemoved=$data")
                    //if (data != null) CoroutineScope(coroutineDispatcher).launch { emit(data) }
                } catch (e: DatabaseException) {}
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                try {
                    val data = snapshot.getValue(PatternHostData::class.java)
                    Log.i(TAG, "onChildMoved=$data")
                    //if (data != null) CoroutineScope(coroutineDispatcher).launch { emit(data) }
                } catch (e: DatabaseException) {}
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
                liveErrors.postValue(error.toException())
            }
        })
    }

}