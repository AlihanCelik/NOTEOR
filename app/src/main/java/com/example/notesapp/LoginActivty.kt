package com.example.notesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowInsetsController

class LoginActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val backgroundBlue=resources.getColor(R.color.blue)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)
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
        val timer=object : CountDownTimer(2000,1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val intent= Intent(this@LoginActivty,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        timer.start()
    }

}