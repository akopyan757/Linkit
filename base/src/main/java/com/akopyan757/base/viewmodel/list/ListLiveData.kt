package com.akopyan757.base.viewmodel.list

import androidx.lifecycle.MediatorLiveData
import com.akopyan757.base.viewmodel.DiffItemObservable

class ListLiveData<T : DiffItemObservable> : MediatorLiveData<ListHolder<T>>() {

    private val items = mutableListOf<T>()

    fun getList(): List<T> = synchronized(this) {
        return items
    }

    fun init(values: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        items.clear()
        items.addAll(values)
        value = ListHolder(values, ListChangeStrategy.Initialized(after))
    }

    fun insert(values: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        val oldLength = items.size
        items.addAll(values)
        val newLength = items.size - 1
        val insertRange = IntRange(oldLength, newLength)
        val strategy = ListChangeStrategy.RangeInserted(insertRange, after)
        value = ListHolder(items.toList(), strategy)
    }

    fun insert(values: List<T>, index: Int, after: (() -> Unit)? = null) = synchronized(this) {
        items.addAll(index, values)
        val insertRange = IntRange(index, index + values.size - 1)
        val strategy = ListChangeStrategy.RangeInserted(insertRange, after)
        value = ListHolder(items.toList(), strategy)
    }

    fun changeItem(newValue: T, after: (() -> Unit)? = null) = synchronized(this) {
        val index = items.indexOfFirst { it.id() == newValue.id() }
        if (index != -1) return@synchronized
        items.removeAt(index)
        items.add(index, newValue)
        val changeRange = IntRange(index, index)
        val strategy = ListChangeStrategy.RangeChanged(changeRange, after)
        value = ListHolder(items.toList(), strategy)
    }

    fun deleteItem(newValue: T, after: (() -> Unit)? = null) = synchronized(this) {
        val strategy = if (items.size == 1 && items.first() == newValue) {
            items.clear()
            ListChangeStrategy.Cleared(after)
        } else {
            val index = items.indexOfFirst { item -> item.id() == newValue.id() }
            items.removeAt(index)
            val changedRange = IntRange(index, items.size - 1)
            ListChangeStrategy.RangeChanged(changedRange)
        }
        value = ListHolder(items.toList(), strategy)
    }

    fun change(values: List<T>, after: (() -> Unit)? = null) = synchronized(this) {
        value = ListHolder(values, ListChangeStrategy.CustomChanged(after))
    }
}