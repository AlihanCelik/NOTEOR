package com.example.notesapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_create_note.*

class PicturesActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictures_activty)
        val photoView: PhotoView = findViewById(R.id.photo_view)
        val imageUriString = intent.getStringExtra("image")
        val imageUri = Uri.parse(imageUriString)
        Glide.with(this).load(imageUri).into(photoView)
        backButton.setOnClickListener {
            finish()
        }

    }
}


