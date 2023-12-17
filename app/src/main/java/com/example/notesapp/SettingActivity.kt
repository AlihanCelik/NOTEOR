package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.database.TrashDatabase
import com.example.notesapp.entities.Notes
import com.example.notesapp.entities.Trash
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
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
        report.setOnClickListener {
            val bottomSheet =
                BottomSheetDialog(this@SettingActivity, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottomsheet_report,
                findViewById(R.id.bottomSheetReport)
            ) as ConstraintLayout
            bottomSheetView.findViewById<View>(R.id.email_send).setOnClickListener {
                sendEmail(this@SettingActivity)
            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()

        }
        var notes: List<Notes> = emptyList()
        GlobalScope.launch(Dispatchers.Main) {
            let {
               notes = NotesDatabase.getDatabase(this@SettingActivity).noteDao().getAllNotes()
            }
            allNotes_size.text="(${notes.size})"
        }


        var trash: List<Trash> = emptyList()
        GlobalScope.launch(Dispatchers.Main) {
            let {
                trash = TrashDatabase.getDatabase(this@SettingActivity).trashDao().getAllTrash()
            }
            trash_size.text="(${trash.size})"
        }

    }
    fun sendEmail(context: Context) {
        val email = "alihancelikk03@gmail.com"
        val subject = "App Feedback"
        val body = "Android Version: ${Build.VERSION.RELEASE}\n" +
                "SDK Version: ${Build.VERSION.SDK_INT}\n" +
                "App Version: ${getAppVersion(context)}"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)

        try {
            context.startActivity(Intent.createChooser(intent, "Send mail..."))
        } catch (e: Exception) {
            Log.e("Email", "Error sending email", e)
            Toast.makeText(context, "Error sending email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAppVersion(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "N/A"
    }
}