package com.example.notesapp.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.item_notes.view.*
import kotlinx.android.synthetic.main.locked_dialog.view.*
import kotlinx.android.synthetic.main.notelongclick_dialog.view.*
import kotlinx.android.synthetic.main.password_remove_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesAdapter :
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
    fun updateData(newNotesList: List<Notes>) {
        arrList.clear()
        arrList.addAll(newNotesList)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        var password=""
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
                holder.itemView.item_bg.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
            view.add_remove_Favorites.setOnClickListener {
                if(arrList[position].favorite==false){
                    arrList[position].favorite=true
                    holder.itemView.item_fav.visibility=View.VISIBLE

                }else{
                    arrList[position].favorite=false
                    holder.itemView.item_fav.visibility=View.GONE
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
                    val view3 = View.inflate(context, R.layout.password_remove_dialog, null)
                    val builder3 = AlertDialog.Builder(context)
                    builder3.setView(view3)
                    val dialog3 = builder3.create()
                    dialog3.show()
                    dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view3.okRemovePsw.setOnClickListener {
                        password = ""
                        dialog3.dismiss()
                    }
                    view3.cancelRemovePsw.setOnClickListener{
                        dialog3.dismiss()
                    }

                }
                arrList[position].password=password
                GlobalScope.launch(Dispatchers.IO) {
                    NotesDatabase.getDatabase(context).noteDao().updateNote(arrList[position])
                }
                notifyDataSetChanged()
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
        if(arrList[position].password.isNullOrEmpty()){
            holder.itemView.item_psw.visibility=View.GONE
        }else{
            holder.itemView.item_psw.visibility=View.VISIBLE
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
        holder.itemView.item_desc.text = arrList[position].noteText
        holder.itemView.item_date.text = arrList[position].dateTime




    }

    class NotesViewHolder(view:View) : RecyclerView.ViewHolder(view){
        var image: ImageView = itemView.findViewById(R.id.item_img)
    }




}