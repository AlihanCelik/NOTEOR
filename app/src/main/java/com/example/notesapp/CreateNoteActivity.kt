package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_create_note.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

class CreateNoteActivity : AppCompatActivity() {
    var currentDate:String? = null

    var color="blue"
    override fun onCreate(savedInstanceState: Bundle?) {
        val moonBlue = resources.getColor(R.color.moonBlue)
        val moonPink = resources.getColor(R.color.moonPink)
        val moonPurple = resources.getColor(R.color.moonPurple)
        val moonGreen = resources.getColor(R.color.moonGreen)
        val moonRed=resources.getColor(R.color.moonRed)
        val moonYellow=resources.getColor(R.color.moonYellow)
        val sdf =SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate=sdf.format(Date())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        tvDateTime.text=currentDate

        backButton.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        saveButton.setOnClickListener {
            saveNote()
        }
        more.setOnClickListener {
            val bottomSheet =
                BottomSheetDialog(this@CreateNoteActivity, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_note,
                findViewById(R.id.bottomSheet)
            ) as ConstraintLayout
            bottomSheetView.findViewById<View>(R.id.blue).setOnClickListener {
                color="blue"
                colorView.setBackgroundColor(moonBlue)

            }
            bottomSheetView.findViewById<View>(R.id.pink).setOnClickListener {
                color="pink"
                colorView.setBackgroundColor(moonPink)

            }
            bottomSheetView.findViewById<View>(R.id.purple).setOnClickListener {
                color="purple"
                colorView.setBackgroundColor(moonPurple)

            }
            bottomSheetView.findViewById<View>(R.id.green).setOnClickListener {
                color="green"
                colorView.setBackgroundColor(moonGreen)

            }
            bottomSheetView.findViewById<View>(R.id.yellow).setOnClickListener {
                color="yellow"
                colorView.setBackgroundColor(moonYellow)

            }
            bottomSheetView.findViewById<View>(R.id.red).setOnClickListener {
                color="red"
                colorView.setBackgroundColor(moonRed)

            }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

    }
    private fun saveNote(){
        if(notes_title.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Note Title is Required", Toast.LENGTH_SHORT).show()
        }
        else if(notes_sub_title.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Note Sub Title is Required",Toast.LENGTH_SHORT).show()

        }
        else if(notes_desc.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Note Description is Required",Toast.LENGTH_SHORT).show()

        }
        else{
            val coroutineScope = CoroutineScope(Dispatchers.Main) // Create a CoroutineScope
            coroutineScope.launch{
                var notes = Notes()
                notes.title=notes_title.text.toString()
                notes.subTitle=notes_sub_title.text.toString()
                notes.noteText=notes_desc.text.toString()
                notes.dateTime=currentDate
                notes.color=color
                applicationContext?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    notes_title.setText("")
                    notes_sub_title.setText("")
                    notes_desc.setText("")
                }
            }
        }

    }
}