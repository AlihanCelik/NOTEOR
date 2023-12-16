package com.example.notesapp.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Item
import kotlinx.android.synthetic.main.item_notelist.view.*

class ListNoteAdapter constructor(
    private var items: MutableList<Item>
) : RecyclerView.Adapter<ListNoteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notelist, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.checkBox_item.isChecked=items[position].isChecked
        holder.itemView.EditText_item.setText(items[position].text)
    }




    override fun getItemCount(): Int {
        return items.size
    }
}