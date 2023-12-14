package com.example.notesapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
         backButton.setOnClickListener {
             finish()
         }
        github.setOnClickListener {
            val url = "https://github.com/AlihanCelik/NOTEOR"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        linkedin.setOnClickListener {
            val url = "https://www.linkedin.com/feed/update/urn:li:activity:7090282773734658048/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        calculationHub.setOnClickListener {
            val url = "https://play.google.com/store/apps/details?id=com.calculationHub.calculatorapp"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        share_noteor.setOnClickListener {
            val url = "https://play.google.com/store/apps/details?id=com.calculationHub.calculatorapp"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            val chooser = Intent.createChooser(shareIntent, "Share using")
            startActivity(chooser)
        }
    }
}