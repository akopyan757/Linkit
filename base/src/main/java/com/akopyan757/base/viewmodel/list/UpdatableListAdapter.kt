package com.akopyan757.base.viewmodel.list

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.base.viewmodel.DiffItemObservable
import java.util.concurrent.CountDownLatch


abstract class UpdatableListAdapter<T : DiffItemObservable> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /** Section: Constants. */

    companion object {
        private const val THREAD_PREFIX = "adapter"
    }

    /** Section: Protected fields. */

    protected var view: RecyclerView? = null

    var items = mutableListOf<T>()
        private set

    private var lastPosition = -1

    private val backgroundHandler: Handler
    private val mainHandler: Handler = Handler(Looper.getMainLooper())

    /** Section: Constructor. */

    init {
        val threadName = THREAD_PREFIX + Integer.toHexString(this.hashCode())
        backgroundHandler = HandlerThread(threadName).run {
            start()
            Handler(looper)
        }
    }

    /** Section: Overriden methods. */

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        view = recyclerView
    }

    override fun getItemCount(): Int = items.size

    /** Section: Update events. */

    fun updateCleared(after: (() -> Unit)? = null) {
        backgroundHandler.post {
            items.clear()
            lastPosition = -1

            awaitMainThread {
                notifyDataSetChanged()
                view?.scheduleLayoutAnimation()
                after?.invoke()
            }
        }
    }

    fun updateInitialized(data: List<T>, after: (() -> Unit)? = null) {
        val list = data.toList()

        backgroundHandler.post {
            items.apply {
                clear()
                addAll(list)
                lastPosition = -1
            }

            awaitMainThread {
                notifyDataSetChanged()
                view?.scheduleLayoutAnimation()
                after?.invoke()
            }
        }
    }

    fun updateCustomChanged(data: List<T>, after: (() -> Unit)? = null) {
        val list = data.toList()

        backgroundHandler.post {
            items.apply {
                if (isNotEmpty()) return@apply

                addAll(list)

                awaitMainThread {
                    notifyDataSetChanged()
                    view?.scheduleLayoutAnimation()
                    after?.invoke()
                }

                return@post
            }

            val callback = DefaultDiffCallback(items, list)
            val result = DiffUtil.calculateDiff(callback)

            items.apply {
                clear()
                addAll(list)
            }

            awaitMainThread {
                result.dispatchUpdatesTo(this@UpdatableListAdapter)
                after?.invoke()
            }
        }
    }

    fun updateRangeChanged(range: IntRange, after: (() -> Unit)? = null) {
        backgroundHandler.post {
            awaitMainThread {
                with(range) {
                    notifyItemRangeChanged(start, endInclusive - start + 1)
                }

                after?.invoke()
            }
        }
    }

    fun updateRangeInserted(data: List<T>, range: IntRange, after: (() -> Unit)? = null) {
        val list = data.toMutableList()

        Handler(Looper.getMainLooper()).post {
            with(range) {
                items = list

                notifyItemRangeInserted(start, endInclusive - start + 1)
                view?.scheduleLayoutAnimation()

                val next = endInclusive + 1
                if (next < items.size) {
                    notifyItemRangeChanged(next, items.size - next)
                }

                after?.invoke()
            }
        }
    }


    private inline fun awaitMainThread(crossinline action: () -> Unit) = CountDownLatch(1).run {
        mainHandler.post {
            action()
            countDown()
        }

        await()
    }
}