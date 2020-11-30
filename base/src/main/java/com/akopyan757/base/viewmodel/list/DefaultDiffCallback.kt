package com.akopyan757.base.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import com.akopyan757.base.viewmodel.DiffItemObservable

class DefaultDiffCallback<T : DiffItemObservable>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean = oldList[p0].id() == newList[p1].id()

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean = oldList[p0].hashCode() == newList[p1].hashCode()
}