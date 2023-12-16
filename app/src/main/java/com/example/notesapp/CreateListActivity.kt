package com.example.notesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.ImageAdapter
import com.example.notesapp.Adapter.LinksAdapter
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Category
import com.example.notesapp.entities.Item
import kotlinx.android.synthetic.main.activity_create_list.*
import kotlinx.android.synthetic.main.activity_create_list.category_name
import kotlinx.android.synthetic.main.activity_create_list.colorView
import kotlinx.android.synthetic.main.activity_create_list.createNote
import kotlinx.android.synthetic.main.activity_create_list.favButton
import kotlinx.android.synthetic.main.activity_create_list.notes_sub_title
import kotlinx.android.synthetic.main.activity_create_list.notes_title
import kotlinx.android.synthetic.main.activity_create_list.reminderlayout
import kotlinx.android.synthetic.main.activity_create_list.tvDateTime
import kotlinx.android.synthetic.main.activity_create_list.tvReminderTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateListActivity : AppCompatActivity() {
    var currentDate:String? = null
    var color="blue"
    var fav=false
    var password=""
    var passwordBoolean=false
    var noteId=-1
    var categoryName=-1
    var reminder: Long? =null
    lateinit var recyclerView: RecyclerView
    lateinit var items_list:MutableList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {


        val moonBlue = resources.getColor(R.color.moonBlue)
        val moonPink = resources.getColor(R.color.moonPink)
        val moonPurple = resources.getColor(R.color.moonPurple)
        val moonGreen = resources.getColor(R.color.moonGreen)
        val moonRed=resources.getColor(R.color.moonRed)
        val moonYellow=resources.getColor(R.color.moonYellow)
        val moonOrange=resources.getColor(R.color.moonOrange)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

        val backgroundYellow=resources.getColor(R.color.background_yellow)
        val backgroundRed=resources.getColor(R.color.background_red)
        val backgroundBlue=resources.getColor(R.color.background_blue)
        val backgroundGreen=resources.getColor(R.color.background_green)
        val backgroundPink=resources.getColor(R.color.background_pink)
        val backgroundPurple=resources.getColor(R.color.background_purple)
        val backgroundOrange=resources.getColor(R.color.background_orange)
        createNotificationChannel()
        currentDate=sdf.format(Date())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)

        recyclerView = findViewById(R.id.recycler_view_itemlist)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        noteId = intent.getIntExtra("itemid",-1)
        tvDateTime.text=currentDate

        if(noteId!=-1){
            GlobalScope.launch(Dispatchers.Main){
                let {
                    var notes = NotesDatabase.getDatabase(this@CreateListActivity).noteDao().getSpecificNote(noteId)
                    if(notes.favorite == true){
                        fav=true
                        favButton.setImageResource(R.drawable.favoriteon)
                    }else{
                        fav=false
                        favButton.setImageResource(R.drawable.favoriteoff)
                    }
                    if(notes.noteCategoryId!=-1){
                        categoryName= notes.noteCategoryId!!
                        GlobalScope.launch(Dispatchers.Main){
                            let {
                                var category= CategoryDatabase.getDatabase(this@CreateListActivity).CategoryDao().getAllCategory()
                                var arrCategory = category as ArrayList<Category>
                                for (arr in arrCategory){
                                    if(arr.id_category==categoryName){
                                        category_name.text=arr.name_category.toString()
                                    }
                                }
                            }

                        }
                    }else{
                        category_name.text="Categories"
                    }

                    if(notes.reminder!=null){
                        reminderlayout.visibility= View.VISIBLE
                        val reminderDateItem = Date(notes.reminder!!)
                        val formattedDateItem = sdf.format(reminderDateItem)
                        tvReminderTime.text=formattedDateItem
                        reminder=notes.reminder
                        if(notes.reminder!! <System.currentTimeMillis()){
                            tvReminderTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }else{
                            tvReminderTime.paintFlags =0
                        }
                    }else{
                        reminderlayout.visibility= View.GONE
                    }
                    items_list= notes.itemList as MutableList<Item>
                    password= notes.password.toString()
                    if(!password.isNullOrEmpty()){
                        passwordBoolean=true
                    }
                    notes_title.setText(notes.title)
                    notes_sub_title.setText(notes.subTitle)
                    notes_desc.setText(notes.noteText)
                    tvDateTime.text=notes.dateTime


                    when (notes.color) {
                        "blue" -> {
                            color = "blue"
                            colorView.setBackgroundColor(moonBlue)
                            createNote.setBackgroundColor(backgroundBlue)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundBlue
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundBlue
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }
                        }
                        "pink" -> {
                            color = "pink"
                            colorView.setBackgroundColor(moonPink)
                            createNote.setBackgroundColor(backgroundPink)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundPink
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundPink
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }

                        }
                        "purple" -> {
                            color = "purple"
                            colorView.setBackgroundColor(moonPurple)
                            createNote.setBackgroundColor(backgroundPurple)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundPurple
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundPurple
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }

                        }
                        "yellow" -> {
                            color = "yellow"
                            colorView.setBackgroundColor(moonYellow)
                            createNote.setBackgroundColor(backgroundYellow)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundYellow
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundYellow
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }

                        }
                        "green" -> {
                            color = "green"
                            colorView.setBackgroundColor(moonGreen)
                            createNote.setBackgroundColor(backgroundGreen)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundGreen
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundGreen
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }

                        }
                        "red" -> {
                            color = "red"
                            colorView.setBackgroundColor(moonRed)
                            createNote.setBackgroundColor(backgroundRed)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundRed
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundRed
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }

                        }
                        "orange" -> {
                            color = "orange"
                            colorView.setBackgroundColor(moonOrange)
                            createNote.setBackgroundColor(backgroundOrange)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.statusBarColor = backgroundOrange
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                window.navigationBarColor = backgroundOrange
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val controller = window.insetsController
                                controller?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }
                        }

                    }
                    initAdapter()
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = backgroundBlue
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = backgroundBlue
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        }

    }


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name :CharSequence="ReminderChannel"
            val description="Channel for Alarm Manager"
            val importance= NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel("foxandroid",name,importance)
            channel.description=description
            val notificationManeger=getSystemService(NotificationManager::class.java)
            notificationManeger.createNotificationChannel(channel)
        }
    }
    private fun initAdapter() {

    }

}