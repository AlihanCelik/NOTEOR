package com.example.notesapp.Adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.database.TrashDatabase
import com.example.notesapp.entities.Notes
import com.example.notesapp.entities.Trash
import kotlinx.android.synthetic.main.delete_permi_dialog.view.*
import kotlinx.android.synthetic.main.item_notes.view.*
import kotlinx.android.synthetic.main.trashlongclick_dialog.view.*
import kotlinx.coroutines.*

class TrashAdapter() :
    RecyclerView.Adapter<TrashAdapter.NotesViewHolder>() {
    var arrList = ArrayList<Trash>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {

        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        )
    }
    fun setData(arrNotesList: List<Trash>){
        arrList = ArrayList(arrNotesList)
    }
    fun clearData() {
        arrList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
    fun updateData(newNotesList: List<Trash>) {
        arrList.clear()
        arrList.addAll(newNotesList)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val view = View.inflate(holder.itemView.context, R.layout.trashlongclick_dialog, null)
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setView(view)
            val dialog = builder.create()
            var trash=arrList[position]
            when (trash.color_t) {
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
            view.cancelTrashLong.setOnClickListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
            view.delete.setOnClickListener {
                val view3 = View.inflate(context, R.layout.delete_permi_dialog, null)
                val builder3 = AlertDialog.Builder(context)
                builder3.setView(view3)
                val dialog3 = builder3.create()
                dialog3.show()
                dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
                view3.cancel_delete_permi.setOnClickListener{
                    holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                    dialog3.dismiss()
                }
                dialog3.setOnCancelListener {
                    holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                }
                var note=arrList[position]
                view3.yes_delete_permi.setOnClickListener {
                    GlobalScope.launch {
                        note.id?.let { it1 ->
                            TrashDatabase.getDatabase(context).trashDao().deleteSpecificTrash(
                                it1
                            )
                        }
                    }
                    arrList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                    dialog3.dismiss()
                    holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                }
                dialog.dismiss()
            }
            view.restore.setOnClickListener {
                dialog.dismiss()
                var trash=arrList[position]
                GlobalScope.launch {
                    var notes = Notes()
                    notes.title = trash.title_t
                    notes.subTitle = trash.subTitle_t
                    notes.noteText = trash.noteText_t
                    notes.dateTime = trash.dateTime_t
                    notes.create_dateTime = trash.create_dateTime_t
                    notes.color = trash.color_t
                    notes.itemList=trash.itemList_t
                    notes.noteCategoryId=trash.noteCategory_t
                    notes.imgPath = trash.imgPath_t
                    notes.noteCategoryId=trash.noteCategory_t
                    notes.webLink = trash.webLink_t
                    notes.favorite = trash.favorite_t
                    notes.password = trash.password_t
                    context?.let {
                        trash.id?.let { it1 ->
                            TrashDatabase.getDatabase(it).trashDao().deleteSpecificTrash(
                                it1
                            )
                        }
                        NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    }
                }
                arrList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                Toast.makeText(context, "Note Restored", Toast.LENGTH_SHORT).show()
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
            dialog.setOnCancelListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
            true

        }
        if(arrList[position].webLink_t.isNullOrEmpty()){
            holder.itemView.itemLinkLayout.visibility=View.GONE
        }else{
            if(!arrList[position].password_t.isNullOrEmpty()){
                holder.itemView.itemLinkLayout.visibility=View.GONE
            }else{
                holder.itemView.itemWebLink.text= arrList[position].webLink_t?.get(0)
                holder.itemView.itemLinkLayout.visibility=View.VISIBLE

            }

        }

        if(arrList[position].itemList_t.isNullOrEmpty()){
            holder.itemView.list_layout.visibility=View.GONE
            holder.itemView.item_desc.visibility=View.VISIBLE

        }else{
            holder.itemView.list_layout.visibility=View.VISIBLE
            holder.itemView.item_desc.visibility=View.GONE
            if(arrList[position].itemList_t!!.size==3){
                holder.itemView.list3_layout.visibility=View.VISIBLE
                holder.itemView.list2_layout.visibility=View.VISIBLE
                holder.itemView.list1_layout.visibility=View.VISIBLE
                holder.itemView.list_more_text.visibility=View.GONE
                holder.itemView.list1_text.text=arrList[position].itemList_t!!.get(0).text
                holder.itemView.list2_text.text=arrList[position].itemList_t!!.get(1).text
                holder.itemView.list3_text.text=arrList[position].itemList_t!!.get(2).text
                if(arrList[position].itemList_t!!.get(0).isChecked){
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

                if(arrList[position].itemList_t!!.get(1).isChecked){
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

                if(arrList[position].itemList_t!!.get(2).isChecked){
                    holder.itemView.list3_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list3_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

            }else if(arrList[position].itemList_t!!.size==2){
                holder.itemView.list3_layout.visibility=View.GONE
                holder.itemView.list2_layout.visibility=View.VISIBLE
                holder.itemView.list1_layout.visibility=View.VISIBLE
                holder.itemView.list_more_text.visibility=View.GONE
                holder.itemView.list1_text.text=arrList[position].itemList_t!!.get(0).text
                holder.itemView.list2_text.text=arrList[position].itemList_t!!.get(1).text
                if(arrList[position].itemList_t!!.get(0).isChecked){
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

                if(arrList[position].itemList_t!!.get(1).isChecked){
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }
            }else if(arrList[position].itemList_t!!.size>3){
                holder.itemView.list3_layout.visibility=View.VISIBLE
                holder.itemView.list2_layout.visibility=View.VISIBLE
                holder.itemView.list1_layout.visibility=View.VISIBLE
                holder.itemView.list_more_text.visibility=View.VISIBLE
                holder.itemView.list_more_text.text= "${(arrList[position].itemList_t!!.size)-3} more items"
                holder.itemView.list1_text.text=arrList[position].itemList_t!!.get(0).text
                holder.itemView.list2_text.text=arrList[position].itemList_t!!.get(1).text
                holder.itemView.list3_text.text=arrList[position].itemList_t!!.get(2).text
                if(arrList[position].itemList_t!!.get(0).isChecked){
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

                if(arrList[position].itemList_t!!.get(1).isChecked){
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list2_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

                if(arrList[position].itemList_t!!.get(2).isChecked){
                    holder.itemView.list3_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list3_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }
            }else{
                holder.itemView.list3_layout.visibility=View.GONE
                holder.itemView.list2_layout.visibility=View.GONE
                holder.itemView.list1_layout.visibility=View.VISIBLE
                holder.itemView.list_more_text.visibility=View.GONE
                holder.itemView.list1_text.text=arrList[position].itemList_t!!.get(0).text
                if(arrList[position].itemList_t!!.get(0).isChecked){
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_24)
                }else{
                    holder.itemView.list1_check.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
                }

            }
        }

        if(arrList[position].imgPath_t.isNullOrEmpty()){
            holder.itemView.item_layout_img.visibility=View.GONE
        }else{
            if(!arrList[position].password_t.isNullOrEmpty()){
                if(!arrList[position].noteText_t.isNullOrEmpty()){
                    holder.itemView.item_desc.visibility=View.VISIBLE
                }
            }else{
                holder.itemView.item_layout_img.visibility=View.VISIBLE
                Glide.with(context).load(arrList[position].imgPath_t?.get(0)).into(holder.image)
                if(arrList[position].imgPath_t?.size == 1){
                    holder.itemView.multiple_img_icon.visibility=View.GONE
                }else{
                    holder.itemView.multiple_img_icon.visibility=View.VISIBLE
                }
            }

        }
        holder.itemView.item_reminder_l.visibility=View.GONE
        if(arrList[position].favorite_t==true){
            holder.itemView.item_fav.visibility=View.VISIBLE
        }else{
            holder.itemView.item_fav.visibility=View.INVISIBLE
        }
        if(arrList[position].password_t.isNullOrEmpty()){
            holder.itemView.item_desc.visibility=View.VISIBLE
            holder.itemView.hidden.visibility=View.GONE
            holder.itemView.item_psw.visibility=View.GONE
        }else{
            holder.itemView.list_layout.visibility=View.GONE
            holder.itemView.item_desc.visibility=View.GONE
            holder.itemView.hidden.visibility=View.VISIBLE
            holder.itemView.item_psw.visibility=View.VISIBLE
        }

        when (arrList[position].color_t) {
            "blue" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonBlue))
            "pink" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPink))
            "purple" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPurple))
            "yellow" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonYellow))
            "green" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonGreen))
            "red" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonRed))
            "orange" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonOrange))

        }
        holder.itemView.item_title.text = arrList[position].title_t
        holder.itemView.item_desc.text = arrList[position].noteText_t
        holder.itemView.item_date.text = arrList[position].dateTime_t

    }
    class NotesViewHolder(view:View) : RecyclerView.ViewHolder(view){
        var image: ImageView = itemView.findViewById(R.id.item_img)
    }




}