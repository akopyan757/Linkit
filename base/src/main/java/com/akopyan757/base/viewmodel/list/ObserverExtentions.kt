package com.akopyan757.base.viewmodel.list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.DiffItemObservable

fun <T : DiffItemObservable> LiveData<ListHolder<T>>.observeList(
    lifecycleOwner: LifecycleOwner,
    adapter: UpdatableListAdapter<T>,
    afterError: (() -> Unit)? = null
) {
    observe(lifecycleOwner, { holder ->
        val data = holder.data
        try {
            when (val strategy = holder.strategy) {
                is ListChangeStrategy.Initialized -> {
                    adapter.updateInitialized(data, strategy.after)
                }

                is ListChangeStrategy.CustomChanged -> {
                    adapter.updateCustomChanged(data, strategy.after)
                }

                is ListChangeStrategy.RangeChanged -> {
                    adapter.updateRangeChanged(strategy.range, strategy.after)
                }

                is ListChangeStrategy.RangeInserted -> {
                    adapter.updateRangeInserted(data, strategy.range, strategy.after)
                    afterError?.invoke()
                }

                is ListChangeStrategy.Cleared -> {
                    adapter.updateCleared(strategy.after)
                }
            }
        } catch (e: Exception) {
            afterError?.invoke()
        }
        holder.strategy.after = null
    })
}