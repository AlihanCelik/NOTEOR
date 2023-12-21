package com.example.notesapp

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Adapter.ListNoteAdapter

class ItemTouchHelperCallbackNoteList( private val adapter:ListNoteAdapter) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        val copiedList = ArrayList(adapter.items)

        val movedItem = copiedList[fromPosition]
        copiedList.removeAt(fromPosition)
        copiedList.add(toPosition, movedItem)


        
        // Yer değiştirdikten sonra sıralamayı güncelle
        adapter.notifyItemMoved(fromPosition, toPosition)

        // Liste üzerinde sıralamayı güncelle
        for (i in 0 until copiedList.size) {
            copiedList[i].order = i
        }
        adapter.updateData(copiedList)

        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

}