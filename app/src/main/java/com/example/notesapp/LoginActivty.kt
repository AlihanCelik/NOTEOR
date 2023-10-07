package com.example.notesapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowInsetsController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LoginActivty : AppCompatActivity() {
    private val PERMISSION_CODE = 1001
    private val permissionId=14
    private var permissionList=
        if(Build.VERSION.SDK_INT>=33) {
            arrayListOf(android.Manifest.permission.READ_MEDIA_IMAGES,android.Manifest.permission.POST_NOTIFICATIONS)
        }else{
            arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        val backgroundBlue=resources.getColor(R.color.blue)
        val backgroundPurple=resources.getColor(R.color.purple)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = backgroundBlue
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
        val timer=object : CountDownTimer(2000,1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (hasPermissions()) {
                    val intent= Intent(this@LoginActivty,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    requestPermissions()
                }

            }

        }
        timer.start()
    }
    private fun hasPermissions(): Boolean {
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permissionList.toTypedArray(),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                val intent= Intent(this@LoginActivty,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {

            }
        }
    }

}