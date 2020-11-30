package com.akopyan757.base.viewmodel.list

import android.util.Log
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.DiffItemObservable

class ListLiveData<T : DiffItemObservable> : LiveData<ListHolder<T>>() {

    fun getList(): List<T> = synchronized(this) {
        return super.getValue()?.data ?: emptyList()
    }

    fun equalLists(comparedList: List<T>): Boolean = synchronized(this)  {
        val list = super.getValue()?.data ?: emptyList()

        if (list.size != comparedList.size)
            return false

        return list.zip(comparedList).all { (elt1, elt2) -> elt1 == elt2 }
    }

    fun init(values: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        value = ListHolder(values, ListChangeStrategy.Initialized(after))
    }

    fun insert(newValues: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        val list = getCopyOfList().apply {
            addAll(newValues)
        }
        val strategy = ListChangeStrategy.RangeInserted(
            IntRange(list.size - newValues.size, list.size - 1), after
        )
        value = ListHolder(list, strategy)
    }

    fun insert(
        newValues: List<T>,
        index: Int,
        after: (() -> Unit)? = null
    ) = synchronized(this) {
        val list = getCopyOfList().apply {
            addAll(index, newValues)
        }
        val strategy = ListChangeStrategy.RangeInserted(
            IntRange(index, index + newValues.size - 1), after
        )
        value = ListHolder(list, strategy)
    }

    fun insertWithChanged(
        newValue: T,
        index: Int,
        after: (() -> Unit)? = null
    ) = synchronized(this) {
        val oldList = super.getValue()?.data ?: emptyList()
        val oldValue = oldList.firstOrNull { it.id() == newValue.id() }
        val list = oldList.toMutableList().apply {
            remove(oldValue)
            add(index, newValue)
        }
        Log.i("LIST_LIVE_DATA", "Value: $newValue")

        value = ListHolder(list, ListChangeStrategy.CustomChanged(after))
    }

    fun changeItem(item: T, after: (() -> Unit)? = null) = synchronized(this) {
        val list = getCopyOfList().apply {
            val index = indexOfFirst { it.id() == item.id() }
            set(index, item)
        }
        changeList(list, after)
    }

    fun deleteItem(item: T, after: (() -> Unit)? = null) = synchronized(this) {
        val list = getCopyOfList()
        if (list.size == 1) {
            clearList()
        } else {
            list.remove(item)
            changeList(list, after)
        }
    }

    fun change(values: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        changeList(values, after)
    }

    fun clear() = synchronized(this) {
        clearList()
    }

    private fun getCopyOfList(): MutableList<T> {
        return super.getValue()?.data?.toMutableList() ?: mutableListOf()
    }

    private fun changeList(values: List<T>, after: (() -> Unit)? = null) {
        value = ListHolder(values, ListChangeStrategy.CustomChanged(after))
    }

    private fun clearList() {
        value = ListHolder(emptyList(), ListChangeStrategy.Cleared())
    }
}