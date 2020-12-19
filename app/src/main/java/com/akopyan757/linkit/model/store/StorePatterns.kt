package com.akopyan757.linkit.model.store

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.PatternHostData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class StorePatterns: KoinComponent {

    companion object {
        private const val TAG = "STORE_PATTERNS"
    }

    private val reference: DatabaseReference by inject(named(Config.PATTERNS))

    private val patterns = mutableListOf<PatternHostData>()

    private val livePatterns = MutableLiveData<List<PatternHostData>>()
    private val liveErrors = MutableLiveData<Exception>()

    fun getLiveError(): LiveData<Exception> = liveErrors
    fun getLivePatterns(): LiveData<List<PatternHostData>> = livePatterns

    fun removeObserveItem(patternHostData: PatternHostData) {
        patterns.remove(patternHostData)
    }

    fun fetchData() {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(PatternHostData::class.java)?.also { data ->
                    patterns.add(data)
                    Log.i(TAG, "patterns.size=${patterns.size} data=${data}")
                    livePatterns.value = patterns
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(PatternHostData::class.java)?.also { data ->
                    patterns.add(data)
                    Log.i(TAG, "patterns.size=${patterns.size} data=${data}")
                    livePatterns.value = patterns
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val data = snapshot.getValue(PatternHostData::class.java)
                Log.i(TAG, "onChildRemoved=$data")
                //if (data != null) CoroutineScope(coroutineDispatcher).launch { emit(data) }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(PatternHostData::class.java)
                Log.i(TAG, "onChildMoved=$data")
                //if (data != null) CoroutineScope(coroutineDispatcher).launch { emit(data) }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
                liveErrors.postValue(error.toException())
            }
        })
    }

}