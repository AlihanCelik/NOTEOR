package com.example.notesapp

import android.graphics.Color
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
    var notesAdapter: NotesAdapter = NotesAdapter(2)
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
        GlobalScope.launch(Dispatchers.Main){
            let {
                var notes: List<Notes> = emptyList()

                notes = NotesDatabase.getDatabase(this@ReminderActivtiy).noteDao().getAllNotesWithAllReminders().asReversed()
                val arrNotes = notes.toMutableList()
                notesAdapter!!.setData(arrNotes)
                recycler_view_reminders.adapter = notesAdapter
            }

        }
        all_reminder.setOnClickListener {
            all_reminder.setBackgroundResource(R.drawable.background_blue_select)
            remaining_reminder.setBackgroundResource(R.drawable.background_font)
            done_reminder.setBackgroundResource(R.drawable.background_font)
            all_reminder_text.setTextColor(resources.getColor(R.color.white))
            remaining_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            done_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            updateRecyclerView("all")

        }
        remaining_reminder.setOnClickListener {
            remaining_reminder.setBackgroundResource(R.drawable.background_blue_select)
            all_reminder.setBackgroundResource(R.drawable.background_font)
            done_reminder.setBackgroundResource(R.drawable.background_font)
            remaining_reminder_text.setTextColor(resources.getColor(R.color.white))
            all_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            done_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            updateRecyclerView("remaining")

        }
        done_reminder.setOnClickListener {
            done_reminder.setBackgroundResource(R.drawable.background_blue_select)
            all_reminder.setBackgroundResource(R.drawable.background_font)
            remaining_reminder.setBackgroundResource(R.drawable.background_font)
            done_reminder_text.setTextColor(resources.getColor(R.color.white))
            all_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            remaining_reminder_text.setTextColor(resources.getColor(R.color.grey2))
            updateRecyclerView("done")
        }

    }

    fun updateRecyclerView(sortType:String) {
        if(sortType=="all"){
            GlobalScope.launch(Dispatchers.Main) {
               let {
                    var notes = NotesDatabase.getDatabase(this@ReminderActivtiy).noteDao().getAllNotesWithAllReminders().asReversed()
                    notesAdapter.updateData(notes)
                }
            }
        }else if(sortType=="remaining"){
            GlobalScope.launch(Dispatchers.Main) {
                let {
                    var notes = NotesDatabase.getDatabase(this@ReminderActivtiy).noteDao().getAllNotesWithRemainingeReminders(System.currentTimeMillis())
                        .asReversed()
                    notesAdapter.updateData(notes)
                }
            }
        }else{
            GlobalScope.launch(Dispatchers.Main) {
                let {
                    var notes = NotesDatabase.getDatabase(this@ReminderActivtiy).noteDao().getAllNotesWithDoneReminders(System.currentTimeMillis())
                        .asReversed()
                    notesAdapter.updateData(notes)
                }
            }
        }
    }


}