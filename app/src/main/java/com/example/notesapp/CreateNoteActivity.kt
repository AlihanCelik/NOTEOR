package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_note.*
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sdf =SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate=sdf.format(Date())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        tvDateTime.text=currentDate

        backButton.setOnClickListener {
            finish()
        }

    }
}