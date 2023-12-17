package com.example.notesapp.Adapter


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Item
import kotlinx.android.synthetic.main.item_notelist.view.*

class ListNoteAdapter constructor(
    private var items: MutableList<Item>
) : RecyclerView.Adapter<ListNoteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editTextItem: EditText = itemView.EditText_item
        val checkBoxItem: CheckBox = itemView.checkBox_item

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
        }
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

    fun addItem(newItem: Item) {
        items.add(newItem)
        notifyDataSetChanged()
    }




    override fun getItemCount(): Int {
        return items.size
    }
}