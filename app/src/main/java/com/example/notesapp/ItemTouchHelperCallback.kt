package com.example.notesapp
import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Adapter.CategoryActivtiyAdapter
import com.example.notesapp.database.CategoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemTouchHelperCallback(private val context: Context, private val adapter: CategoryActivtiyAdapter) : ItemTouchHelper.Callback() {

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
        val movedItem = adapter.arrList[fromPosition]
        adapter.arrList.removeAt(fromPosition)
        adapter.arrList.add(toPosition, movedItem)

        // Yer değiştirdikten sonra sıralamayı güncelle
        adapter.notifyItemMoved(fromPosition, toPosition)

        for (i in 0 until adapter.arrList.size) {
            adapter.arrList[i].order_category = i
        }

        // Veritabanını güncelle
        GlobalScope.launch(Dispatchers.IO) {
            for (category in adapter.arrList) {
                CategoryDatabase.getDatabase(context).CategoryDao().updateCategory(category)
            }
        }
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}