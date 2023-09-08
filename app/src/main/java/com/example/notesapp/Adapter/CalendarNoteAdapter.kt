package com.example.notesapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.item_calendar_notes.view.*
import kotlinx.android.synthetic.main.notelongclick_dialog.view.*

class CalendarNoteAdapter :
    RecyclerView.Adapter<CalendarNoteAdapter.CalendarNotesViewHolder>() {

    var arrList = ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarNotesViewHolder {
        return CalendarNotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_notes,parent,false)
        )
    }

    override fun onBindViewHolder(holder: CalendarNotesViewHolder, position: Int) {
        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
        val context = holder.itemView.context
        holder.itemView.setOnLongClickListener {
            val view = View.inflate(holder.itemView.context, R.layout.notelongclick_dialog, null)
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setView(view)
            val dialog = builder.create()
            when (arrList[position].color) {
                "blue" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_blue))
                "pink" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_pink))
                "purple" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_purple))
                "yellow" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_yellow))
                "green" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_green))
                "red" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_red))
                "orange" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_orange))

            }

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            view.cancelLong.setOnClickListener {
                holder.itemView.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
            dialog.setOnCancelListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
            true

        }

        if(arrList[position].webLink.isNullOrEmpty()){
            holder.itemView.itemLinkLayout.visibility=View.GONE
        }else{
            holder.itemView.itemWebLink.text= arrList[position].webLink?.get(0)
            holder.itemView.itemLinkLayout.visibility=View.VISIBLE
        }

        if(arrList[position].imgPath.isNullOrEmpty()){
            holder.itemView.item_layout_img.visibility=View.GONE
        }else{
            holder.itemView.item_layout_img.visibility=View.VISIBLE
            Glide.with(context).load(arrList[position].imgPath?.get(0)).into(holder.image)
            if(arrList[position].imgPath?.size == 1){
                holder.itemView.multiple_img_icon.visibility=View.GONE
            }else{
                holder.itemView.multiple_img_icon.visibility=View.VISIBLE
            }
        }
        if(arrList[position].password.isNullOrEmpty()){
            holder.itemView.item_psw.visibility=View.GONE
        }else{
            holder.itemView.item_psw.visibility=View.VISIBLE
        }

        if(arrList[position].imgPath.isNullOrEmpty()){
            holder.itemView.item_layout_img.visibility=View.GONE
        }else{
            holder.itemView.item_layout_img.visibility=View.VISIBLE
        }
        if(arrList[position].favorite==true){
            holder.itemView.item_fav.visibility=View.VISIBLE
        }else{
            holder.itemView.item_fav.visibility=View.GONE
        }

        when (arrList[position].color) {
            "blue" -> {
                holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonBlue))
            }
            "pink" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPink))
            "purple" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPurple))
            "yellow" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonYellow))
            "green" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonGreen))
            "red" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonRed))
            "orange" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonOrange))

        }
        holder.itemView.item_title.text = arrList[position].title
        holder.itemView.item_desc.text = arrList[position].noteText
    }



    fun setData(arrNotesList: List<Notes>){
        arrList = arrNotesList as ArrayList<Notes>
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun updateData(newNotesList: List<Notes>) {
        arrList.clear()
        arrList.addAll(newNotesList)
        notifyDataSetChanged()
    }





    class CalendarNotesViewHolder(view:View) : RecyclerView.ViewHolder(view){
        var image: ImageView = itemView.findViewById(R.id.item_img)

    }




}