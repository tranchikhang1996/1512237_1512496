package com.example.highschoolmathsolver.ui.history

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.ui.history.adapater.HistoryAdapter


class RecyclerItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is HistoryAdapter.HistoryAbstractViewHolder) {
            listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder is HistoryAdapter.HistoryAbstractViewHolder) {
            val foregroundView = viewHolder.foreGroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder is HistoryAdapter.HistoryAbstractViewHolder) {
            val foregroundView = viewHolder.foreGroundView
            ItemTouchHelper.Callback.getDefaultUIUtil()
                .onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is HistoryAdapter.HistoryAbstractViewHolder) {
            val foregroundView = viewHolder.foreGroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (viewHolder is HistoryAdapter.HistoryAbstractViewHolder) {
            val foregroundView = (viewHolder).foreGroundView
            ItemTouchHelper.Callback.getDefaultUIUtil()
                .onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }

}