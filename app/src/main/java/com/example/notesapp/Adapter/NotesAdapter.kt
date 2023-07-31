package com.example.notesapp.Adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var arrList = ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        )
    }
    fun setData(arrNotesList: List<Notes>){
        arrList = arrNotesList as ArrayList<Notes>
    }

    override fun getItemCount(): Int {
        return arrList.size
    }



    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {

        holder.itemView.item_title.text = arrList[position].title
        holder.itemView.item_desc.text = arrList[position].noteText
        holder.itemView.item_date.text = arrList[position].dateTime



    }

    class NotesViewHolder(view:View) : RecyclerView.ViewHolder(view){

    }




}