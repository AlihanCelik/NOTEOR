package com.example.notesapp.Adapter

import android.content.ClipData
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.CreateNoteActivity
import com.example.notesapp.R
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.database.TrashDatabase
import com.example.notesapp.entities.Category
import com.example.notesapp.entities.Notes
import com.example.notesapp.entities.Trash
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.delete_permi_dialog.view.*
import kotlinx.android.synthetic.main.enter_psw_dialog.view.*
import kotlinx.android.synthetic.main.item_images.view.*
import kotlinx.android.synthetic.main.item_notes.view.*
import kotlinx.android.synthetic.main.item_notes.view.item_layout_img
import kotlinx.android.synthetic.main.locked_dialog.view.*
import kotlinx.android.synthetic.main.notelongclick_dialog.view.*
import kotlinx.android.synthetic.main.password_remove_dialog.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(val frag:Int) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var arrList = ArrayList<Notes>()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {

        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        )
    }
    fun setData(arrNotesList: List<Notes>){
        arrList = ArrayList(arrNotesList)
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
    fun updateData(newNotesList: List<Notes>) {
        arrList.clear()
        arrList.addAll(newNotesList)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        var password=""
        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            when (arrList[position].color) {
                "blue" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_blue))
                "pink" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_pink))
                "purple" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_purple))
                "yellow" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_yellow))
                "green" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_green))
                "red" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_red))
                "orange" -> holder.itemView.item_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.background_orange))

            }
            if(arrList[position].password.isNullOrEmpty()){
                val intent=Intent(context,CreateNoteActivity::class.java)
                intent.putExtra("itemid",arrList[position].id)
                println(arrList[position].id)
                context.startActivity(intent)
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }else{
                val view = View.inflate(holder.itemView.context, R.layout.enter_psw_dialog,null)
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
                view.enter_passwordContainer.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_green_dark
                        )
                    )
                )
                view.enter_passwordContainer.helperText="Enter Password"
                view.enter_okeylock.setOnClickListener {
                    if(arrList[position].password==view.enter_passwordEditText.text.toString()){
                        view.enter_passwordContainer.setHelperTextColor(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.holo_green_dark
                                )
                            )
                        )
                        view.enter_passwordContainer.helperText="Successful"
                        val intent=Intent(context,CreateNoteActivity::class.java)
                        intent.putExtra("itemid",arrList[position].id)
                        println(arrList[position].id)
                        dialog.dismiss()
                        context.startActivity(intent)
                        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                    }else{
                        view.enter_passwordContainer.helperText="Wrong Password"
                        view.enter_passwordContainer.setHelperTextColor(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.holo_red_dark
                                )
                            )
                        )
                    }
                }
            dialog.setOnCancelListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
        }


        }
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
            if(arrList[position].reminder!=null){
                view.dialogReminderLayout.visibility=View.VISIBLE
                val formattedDateItem = sdf.format(Date(arrList[position].reminder!!))
               view.dialogReminderDate.text= formattedDateItem
                if(arrList[position].reminder!! <System.currentTimeMillis()){
                    view.dialogReminderDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }else{
                    view.dialogReminderDate.paintFlags = 0
                }
            }else{
                view.dialogReminderLayout.visibility=View.GONE
            }
            if(arrList[position].noteCategoryId!=-1){
                view.dialog_category_ly.visibility=View.VISIBLE
                GlobalScope.launch(Dispatchers.Main){
                    let {
                        var category=
                            CategoryDatabase.getDatabase(context).CategoryDao().getAllCategory()
                        var arrCategory = category as java.util.ArrayList<Category>
                        for (arr in arrCategory){
                            if(arr.id_category==arrList[position].noteCategoryId){
                                view.dialog_category_name.text=arr.name_category.toString()
                            }
                        }
                    }
                }
            }else{
               view.dialog_category_ly.visibility=View.GONE
            }
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            view.cancelLong.setOnClickListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
            view.share.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND_MULTIPLE

                // Metin verileri
                val text = arrList[position].title + "\n" + arrList[position].subTitle + "\n" + arrList[position].noteText
                intent.putExtra(Intent.EXTRA_TEXT, text)

                // Resim verileri
                val imgPaths = arrList[position].imgPath

                if (imgPaths != null && imgPaths.isNotEmpty()) {
                    val imageUris = ArrayList<Uri>(imgPaths)

                    // Set the ClipData to include both text and images
                    val clipData = ClipData.newPlainText("text", text)

                    // Add multiple image URIs to ClipData
                    for (uri in imageUris) {
                        clipData.addItem(ClipData.Item(uri))
                    }

                    intent.clipData = clipData
                    intent.type = "image/*"
                }

                // Create a chooser
                val chooser = Intent.createChooser(intent, "Share using ...")
                context.startActivity(chooser)
            }
            view.delete.setOnClickListener {
                dialog.dismiss()

                if(arrList[position].password.isNullOrEmpty()){

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
                            var trash = Trash()
                            trash.title_t = note.title
                            trash.subTitle_t = note.subTitle
                            trash.noteText_t = note.noteText
                            trash.dateTime_t = note.dateTime
                            trash.create_dateTime_t=note.create_dateTime
                            trash.color_t = note.color
                            trash.noteCategory_t=note.noteCategoryId
                            trash.imgPath_t = note.imgPath
                            trash.webLink_t = note.webLink
                            trash.favorite_t = note.favorite
                            trash.password_t = note.password
                            context?.let {
                                TrashDatabase.getDatabase(it).trashDao().insertTrash(trash)
                                note.id?.let { it1 ->
                                    NotesDatabase.getDatabase(it).noteDao().deleteSpecificNoteWithReminder(context,
                                        it1
                                    )
                                }

                            }
                        }
                        arrList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                        Toast.makeText(context, "Note moved to trash", Toast.LENGTH_SHORT).show()
                        dialog3.dismiss()
                        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                    }
                }else{
                    val view4 = View.inflate(context, R.layout.enter_psw_dialog, null)
                    val builder4 = AlertDialog.Builder(context)
                    builder4.setView(view4)
                    val dialog4 = builder4.create()
                    dialog4.show()
                    dialog4.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view4.enter_passwordContainer.setHelperTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_green_dark
                            )
                        )
                    )
                    dialog4.setOnCancelListener {
                        holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                    }
                    view4.enter_passwordContainer.helperText="Enter Password"
                    view4.enter_okeylock.setOnClickListener {
                        if(arrList[position].password==view4.enter_passwordEditText.text.toString()){
                            view4.enter_passwordContainer.setHelperTextColor(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.holo_green_dark
                                    )
                                )
                            )
                            view4.enter_passwordContainer.helperText="Successful"
                            dialog4.dismiss()
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
                                    println(note.noteText)
                                    var trash = Trash()
                                    trash.title_t = note.title
                                    trash.subTitle_t = note.subTitle
                                    trash.noteText_t = note.noteText
                                    trash.dateTime_t = note.dateTime
                                    trash.create_dateTime_t=note.create_dateTime
                                    trash.color_t = note.color
                                    trash.noteCategory_t=note.noteCategoryId
                                    trash.imgPath_t = note.imgPath
                                    trash.webLink_t = note.webLink
                                    trash.favorite_t = note.favorite
                                    trash.password_t = note.password

                                    context?.let {
                                        TrashDatabase.getDatabase(it).trashDao().insertTrash(trash)
                                        note.id?.let { it1 ->
                                            NotesDatabase.getDatabase(it).noteDao().deleteSpecificNote(
                                                it1
                                            )
                                        }
                                    }
                                }
                                arrList.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, itemCount)
                                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                                dialog3.dismiss()
                                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                            }
                        }else{
                            view4.enter_passwordContainer.helperText="Wrong Password"
                            view4.enter_passwordContainer.setHelperTextColor(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.holo_red_dark
                                    )
                                )
                            )


                        }

                    }
                }


            }
            view.add_remove_Favorites.setOnClickListener {
                if(arrList[position].favorite==false){
                    arrList[position].favorite=true
                    holder.itemView.item_fav.visibility=View.VISIBLE

                }else{
                    arrList[position].favorite=false
                    holder.itemView.item_fav.visibility=View.INVISIBLE
                }
                GlobalScope.launch(Dispatchers.IO) {
                    NotesDatabase.getDatabase(context).noteDao().updateNote(arrList[position])
                }
                notifyDataSetChanged()
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
            view.lock.setOnClickListener {
                if(arrList[position].password.isNullOrEmpty()){
                    val view2 = View.inflate(context, R.layout.locked_dialog, null)
                    val builder2 = AlertDialog.Builder(context)
                    builder2.setView(view2)
                    val dialog2 = builder2.create()
                    dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog2.show()
                        var confirm_password = ""
                        view2.okeylock.setOnClickListener {
                            if (view2.confirmpasswordContainer.helperText == "Successful" &&
                                view2.passwordContainer.helperText == "Successful" &&
                                view2.confirm_passwordEditText.text.toString() == view2.passwordEditText.text.toString()
                            ) {
                                password = view2.confirm_passwordEditText.text.toString()
                                arrList[position].password=password
                                holder.itemView.item_psw.visibility=View.VISIBLE
                                GlobalScope.launch(Dispatchers.IO) {
                                    NotesDatabase.getDatabase(context).noteDao().updateNote(arrList[position])
                                }
                                notifyDataSetChanged()
                                dialog2.dismiss()
                            } else {
                                view2.confirmpasswordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            android.R.color.holo_red_dark
                                        )
                                    )
                                )
                                view2.confirmpasswordContainer.helperText = "Enter Confirm Password"

                                view2.passwordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            android.R.color.holo_red_dark
                                        )
                                    )
                                )
                                view2.passwordContainer.helperText = "Enter Password"
                            }
                        }
                        view2.passwordEditText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                view2.passwordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            android.R.color.holo_green_dark
                                        )
                                    )
                                )
                                view2.passwordContainer.helperText = "Enter Password"
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                var password = s.toString()
                                if (password.length < 4) {
                                    view2.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view2.passwordContainer.helperText =
                                        "Minimum 4 Character Password"
                                    view2.passwordContainer.error = ""
                                } else if (password.length in 4..10) {
                                    view2.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                android.R.color.holo_green_dark
                                            )
                                        )
                                    )

                                    view2.passwordContainer.helperText = "Successful"
                                } else {
                                    view2.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view2.passwordContainer.helperText =
                                        "Maximum 10 Character Password"
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {
                                confirm_password = view2.passwordEditText.text.toString()
                            }
                        })

                        view2.confirm_passwordEditText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                view2.confirmpasswordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            android.R.color.holo_green_dark
                                        )
                                    )
                                )
                                view2.confirmpasswordContainer.helperText = "Enter Confirm Password"
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (confirm_password != view2.confirm_passwordEditText.text.toString() ||
                                    view2.passwordContainer.helperText != "Successful"
                                ) {
                                    view2.confirmpasswordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view2.confirmpasswordContainer.helperText =
                                        "Must match the previous entry"
                                    view2.confirmpasswordContainer.error = ""
                                } else {
                                    view2.confirmpasswordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                android.R.color.holo_green_dark
                                            )
                                        )
                                    )
                                    view2.confirmpasswordContainer.helperText = "Successful"
                                    view2.confirmpasswordContainer.error = ""
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })


                }else{
                    val view4 = View.inflate(context, R.layout.enter_psw_dialog, null)
                    val builder4 = AlertDialog.Builder(context)
                    builder4.setView(view4)
                    val dialog4 = builder4.create()
                    dialog4.show()
                    dialog4.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view4.enter_passwordContainer.setHelperTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_green_dark
                            )
                        )
                    )
                    view4.enter_passwordContainer.helperText="Enter Password"
                    view4.enter_okeylock.setOnClickListener {
                        if(arrList[position].password==view4.enter_passwordEditText.text.toString()){
                            view4.enter_passwordContainer.setHelperTextColor(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.holo_green_dark
                                    )
                                )
                            )
                            view4.enter_passwordContainer.helperText="Successful"
                            dialog4.dismiss()
                            val view3 = View.inflate(context, R.layout.password_remove_dialog, null)
                            val builder3 = AlertDialog.Builder(context)
                            builder3.setView(view3)
                            val dialog3 = builder3.create()
                            dialog3.show()
                            dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
                            view3.okRemovePsw.setOnClickListener {
                                password = ""
                                arrList[position].password=password
                                GlobalScope.launch(Dispatchers.IO) {
                                    NotesDatabase.getDatabase(context).noteDao().updateNote(arrList[position])
                                }
                                notifyDataSetChanged()
                                holder.itemView.item_psw.visibility=View.GONE
                                dialog3.dismiss()
                            }
                            view3.cancelRemovePsw.setOnClickListener{
                                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                                dialog3.dismiss()
                            }
                            dialog3.setOnCancelListener {
                                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                            }

                        }else{
                            view4.enter_passwordContainer.helperText="Wrong Password"
                            view4.enter_passwordContainer.setHelperTextColor(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.holo_red_dark
                                    )
                                )
                            )


                        }

                    }


                }


                dialog.dismiss()
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
            dialog.setOnCancelListener {
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
            }
            true

        }




        if(arrList[position].webLink.isNullOrEmpty()){
            holder.itemView.itemLinkLayout.visibility=View.GONE
        }else{
            if(!arrList[position].password.isNullOrEmpty()){
                holder.itemView.itemLinkLayout.visibility=View.GONE
            }else{
                holder.itemView.itemWebLink.text= arrList[position].webLink?.get(0)
                holder.itemView.itemLinkLayout.visibility=View.VISIBLE
            }

        }
        if(arrList[position].imgPath.isNullOrEmpty()){
            holder.itemView.item_layout_img.visibility=View.GONE
        }else{
            if(!arrList[position].password.isNullOrEmpty()){
                holder.itemView.item_layout_img.visibility=View.GONE
            }else{
                if(frag!=1){
                    var picture=arrList[position].imgPath?.get(0)
                    holder.itemView.item_layout_img.visibility=View.VISIBLE
                    Glide.with(context).load(picture).into(holder.image)
                    if(arrList[position].imgPath?.size == 1){
                        holder.itemView.multiple_img_icon.visibility=View.GONE
                    }else{
                        holder.itemView.multiple_img_icon.visibility=View.VISIBLE
                    }
                }

            }

        }
        if(arrList[position].favorite==true){
            holder.itemView.item_fav.visibility=View.VISIBLE
        }else{
            holder.itemView.item_fav.visibility=View.INVISIBLE
        }
        if(arrList[position].password.isNullOrEmpty()){
            holder.itemView.item_desc.visibility=View.VISIBLE
            holder.itemView.hidden.visibility=View.GONE
            holder.itemView.item_psw.visibility=View.GONE
        }else{
            holder.itemView.item_desc.visibility=View.GONE
            holder.itemView.hidden.visibility=View.VISIBLE
            holder.itemView.item_psw.visibility=View.VISIBLE
        }
        if(frag==0){
            holder.itemView.item_date_l.visibility=View.VISIBLE
            holder.itemView.item_reminder_l.visibility=View.GONE

        }else if(frag==1){
            holder.itemView.item_date_l.visibility=View.GONE
            holder.itemView.item_image_img.visibility=View.GONE
            holder.itemView.item_reminder_l.visibility=View.GONE
        }else{
            holder.itemView.item_date_l.visibility=View.GONE
            holder.itemView.item_reminder_l.visibility=View.VISIBLE
        }


        if(arrList[position].reminder!=null){
             holder.itemView.item_rame.visibility=View.VISIBLE
             val reminderDateItem = Date(arrList[position].reminder!!)
             val formattedDateItem = sdf.format(reminderDateItem)
             holder.itemView.item_reminder.text=formattedDateItem

         }else{
             holder.itemView.item_rame.visibility=View.GONE
         }

        when (arrList[position].color) {
            "blue" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonBlue))
            "pink" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPink))
            "purple" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonPurple))
            "yellow" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonYellow))
            "green" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonGreen))
            "red" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonRed))
            "orange" -> holder.itemView.item_color.setBackgroundColor(ContextCompat.getColor(context, R.color.moonOrange))

        }

        holder.itemView.item_title.text = arrList[position].title
        holder.itemView.item_desc.text=arrList[position].noteText
        holder.itemView.item_date.text = arrList[position].dateTime




    }

    class NotesViewHolder(view:View) : RecyclerView.ViewHolder(view){
        var image: ImageView = itemView.findViewById(R.id.item_img)

    }




}