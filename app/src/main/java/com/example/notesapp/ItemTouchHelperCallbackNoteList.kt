package com.example.notesapp
import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Adapter.CategoryActivtiyAdapter
import com.example.notesapp.Adapter.ListNoteAdapter
import com.example.notesapp.database.CategoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemTouchHelperCallbackNoteList(private val context: Context, private val adapter: ListNoteAdapter) : ItemTouchHelper.Callback() {

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

        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
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
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}