package com.example.notesapp.Adapter


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Item
import kotlinx.android.synthetic.main.item_notelist.view.*
import java.util.*

class ListNoteAdapter constructor(
   var items: MutableList<Item>,
   var rcw:RecyclerView,var isItemMoveEnabled:Boolean
) : RecyclerView.Adapter<ListNoteAdapter.ViewHolder>() {

    fun updateEnabled(isItemEnabled: Boolean){
        isItemMoveEnabled=isItemEnabled
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Item>) {

        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    val onItemMoveListener = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (isItemMoveEnabled) {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION
                    && fromPosition < items.size && toPosition < items.size
                ) {
                    Collections.swap(items, fromPosition, toPosition)
                    notifyItemMoved(fromPosition, toPosition)



                    return true
                }
            }

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }
        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            super.clearView(recyclerView, viewHolder)
            // Reset the changeDuration when the item is no longer being dragged
            rcw.itemAnimator?.changeDuration = 250
            notifyDataSetChanged()
        }

    }
    val itemTouchHelper = ItemTouchHelper(onItemMoveListener)

    init {
        itemTouchHelper.attachToRecyclerView(rcw)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val editTextItem: EditText = itemView.EditText_item
        val checkBoxItem: CheckBox = itemView.checkBox_item
        val itemSortImageView: ImageView = itemView.findViewById(R.id.item_sort)


        init {

            editTextItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    items[adapterPosition].text = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
                checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
                    items[adapterPosition].isChecked = isChecked
                }


            itemSortImageView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Start drag only when isItemMoveEnabled is true
                        if (isItemMoveEnabled) {
                            itemTouchHelper.startDrag(this@ViewHolder)
                            true
                        } else {
                            false
                        }
                    }
                    else -> false
                }
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notelist, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (isItemMoveEnabled) {
            holder.itemView.item_delete.visibility = View.VISIBLE
            holder.itemView.EditText_item.isEnabled = true
            holder.itemView.item_sort.visibility = View.VISIBLE
        } else {
            holder.itemView.item_delete.visibility = View.GONE
            holder.itemView.EditText_item.isEnabled = false
            holder.itemView.item_sort.visibility = View.GONE
        }

        holder.itemView.checkBox_item.isChecked=items[position].isChecked
        holder.itemView.EditText_item.setText(items[position].text)
        holder.itemView.item_delete.setOnClickListener {
            items.removeAt(position)
            notifyDataSetChanged()
            notifyItemRangeChanged(position, items.size)

        }
    }

    fun addItem() {
        val newItem = Item(isChecked = false, text = "")
        items.add(newItem)
        notifyItemInserted(items.size - 1)
        rcw.scrollToPosition(items.size - 1)
    }




    override fun getItemCount(): Int {
        return items.size
    }


}