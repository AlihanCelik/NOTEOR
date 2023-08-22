package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_create_note.*

class FavoritesActivtity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_activtity)
        backButton.setOnClickListener {
            finish()
        }
    }
}