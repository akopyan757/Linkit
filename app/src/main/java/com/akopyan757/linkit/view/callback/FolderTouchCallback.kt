package com.akopyan757.linkit.view.callback

import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.akopyan757.linkit.R


class FolderTouchCallback(
    private val adapter: ItemTouchHelperAdapter
): ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, ItemTouchHelper.ACTION_STATE_IDLE)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder:
        RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val drawableRes = R.drawable.background_item_child_delete
        val drawable = ContextCompat.getDrawable(itemView.context, drawableRes)
        val offset = itemView.context.resources.getDimensionPixelOffset(R.dimen.marginGiant)
        val left = dX.toInt() + itemView.width - offset
        drawable?.setBounds(left, itemView.top, itemView.right, itemView.bottom)
        drawable?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
