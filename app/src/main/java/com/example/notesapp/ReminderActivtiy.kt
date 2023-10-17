package com.example.notesapp

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.activity_reminder_activtiy.*
import kotlinx.android.synthetic.main.activity_reminder_activtiy.backButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ReminderActivtiy : AppCompatActivity() {

    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter(0)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_activtiy)
        backButton.setOnClickListener {
            finish()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        }
        recycler_view_reminders.setHasFixedSize(true)
        recycler_view_reminders.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        all_reminder.setOnClickListener {
            all_reminder.setBackgroundResource(R.drawable.background_blue_select)
            remaining_reminder.setBackgroundResource(R.drawable.background_font)
            done_reminder.setBackgroundResource(R.drawable.background_font)
            typeReminder("all")

        }

        remaining_reminder.setOnClickListener {
            remaining_reminder.setBackgroundResource(R.drawable.background_blue_select)
            all_reminder.setBackgroundResource(R.drawable.background_font)
            done_reminder.setBackgroundResource(R.drawable.background_font)
            typeReminder("remaining")
        }
        done_reminder.setOnClickListener {
            done_reminder.setBackgroundResource(R.drawable.background_blue_select)
            all_reminder.setBackgroundResource(R.drawable.background_font)
            remaining_reminder.setBackgroundResource(R.drawable.background_font)
            typeReminder("done")
        }

    }
    fun typeReminder(type:String){
        GlobalScope.launch(Dispatchers.Main){
            let {
                var notes = NotesDatabase.getDatabase(this@ReminderActivtiy).noteDao().getAllNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                var ReArr = ArrayList<Notes>()
                for (arr in arrNotes){
                    if(arr.reminder!=null){
                        if(type=="all"){
                            ReArr.add(arr)
                        }else if(type=="remaining"){
                            if(arr.reminder!! >System.currentTimeMillis()){
                                ReArr.add(arr)
                            }
                        }else{
                            if(arr.reminder!! <System.currentTimeMillis()){
                                ReArr.add(arr)
                            }
                        }

                    }
                }
                notesAdapter.setData(ReArr)
                notesAdapter.notifyDataSetChanged()
                recycler_view_reminders.adapter = notesAdapter
            }

        }
    }

}