package com.example.notesapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_create_note.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import java.io.File
import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.WindowInsetsController
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_url.view.*
import java.io.FileOutputStream

class CreateNoteActivity : AppCompatActivity() {
    var currentDate:String? = null
    var getFile: File? = null
    var color="blue"
    var webLink = ""
    var fav=false
    companion object {
        private val READ_STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private val WRITE_STORAGE_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val REQUEST_CODE_READ_STORAGE_PERMISSION = 10
        const val REQUEST_CODE_WRITE_STORAGE_PERMISSION = 20
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val moonBlue = resources.getColor(R.color.moonBlue)
        val moonPink = resources.getColor(R.color.moonPink)
        val moonPurple = resources.getColor(R.color.moonPurple)
        val moonGreen = resources.getColor(R.color.moonGreen)
        val moonRed=resources.getColor(R.color.moonRed)
        val moonYellow=resources.getColor(R.color.moonYellow)
        val moonOrange=resources.getColor(R.color.moonOrange)
        val sdf =SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        val backgroundYellow=resources.getColor(R.color.background_yellow)
        val backgroundRed=resources.getColor(R.color.background_red)
        val backgroundBlue=resources.getColor(R.color.background_blue)
        val backgroundGreen=resources.getColor(R.color.background_green)
        val backgroundPink=resources.getColor(R.color.background_pink)
        val backgroundPurple=resources.getColor(R.color.background_purple)
        val backgroundOrange=resources.getColor(R.color.background_orange)
        currentDate=sdf.format(Date())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        tvDateTime.text=currentDate
        getFile?.let { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            layout_img_preview.visibility = View.VISIBLE
            img_preview.setImageBitmap(bitmap)
        }
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

        backButton.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        saveButton.setOnClickListener {
            saveNote()
        }
        btn_clear_added_img.setOnClickListener {
            getFile = null
            layout_img_preview.visibility = View.GONE
            img_preview.setImageDrawable(null)
        }
        btnDelete.setOnClickListener {
            webLink=""
            layoutWebUrl.visibility=View.GONE
        }
        favButton.setOnClickListener {
            if(!fav){
                fav=true
                favButton.setImageResource(R.drawable.favoriteon)
            }else{
                fav=false
                favButton.setImageResource(R.drawable.favoriteoff)
            }
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
                createNote.setBackgroundColor(backgroundBlue)

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

            }
            bottomSheetView.findViewById<View>(R.id.pink).setOnClickListener {
                color="pink"
                colorView.setBackgroundColor(moonPink)
                createNote.setBackgroundColor(backgroundPink)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundPink
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = backgroundPink
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController
                    controller?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }
            bottomSheetView.findViewById<View>(R.id.purple).setOnClickListener {
                color="purple"
                colorView.setBackgroundColor(moonPurple)
                createNote.setBackgroundColor(backgroundPurple)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundPurple
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

            }
            bottomSheetView.findViewById<View>(R.id.green).setOnClickListener {
                color="green"
                colorView.setBackgroundColor(moonGreen)
                createNote.setBackgroundColor(backgroundGreen)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundGreen
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = backgroundGreen
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController
                    controller?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }
            bottomSheetView.findViewById<View>(R.id.yellow).setOnClickListener {
                color="yellow"
                colorView.setBackgroundColor(moonYellow)
                createNote.setBackgroundColor(backgroundYellow)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundYellow
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = backgroundYellow
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController
                    controller?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }
            bottomSheetView.findViewById<View>(R.id.red).setOnClickListener {
                color="red"
                colorView.setBackgroundColor(moonRed)
                createNote.setBackgroundColor(backgroundRed)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundRed
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = backgroundRed
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController
                    controller?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }
            bottomSheetView.findViewById<View>(R.id.orange).setOnClickListener {
                color="orange"
                colorView.setBackgroundColor(moonOrange)
                createNote.setBackgroundColor(backgroundOrange)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = backgroundOrange
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = backgroundOrange
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController
                    controller?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }

            bottomSheetView.findViewById<View>(R.id.image).setOnClickListener {
                startGallery()
                bottomSheet.dismiss()

            }
            bottomSheetView.findViewById<View>(R.id.link).setOnClickListener {
                val view = View.inflate(this, R.layout.dialog_url, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                view.okey.setOnClickListener {
                    webLink=view.etWebLink.text.toString()
                    layoutWebUrl.visibility=View.VISIBLE
                    tvWebLink.text=webLink
                    dialog.dismiss()
                }
                bottomSheet.dismiss()

            }
            bottomSheetView.findViewById<View>(R.id.mic).setOnClickListener {
                val view = View.inflate(this, R.layout.record_voice_dialog, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                view.okey.setOnClickListener {
                    dialog.dismiss()
                }
                bottomSheet.dismiss()

            }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }
        tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(tvWebLink.text.toString()))
            startActivity(intent)
        }

    }
    private fun uriToFile(uri: Uri?, context: Context): File? {
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File(context.externalCacheDir, "temp_image.jpg")
            tempFile.createNewFile()
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    while (true) {
                        val bytesRead = input.read(buffer)
                        if (bytesRead == -1) break
                        output.write(buffer, 0, bytesRead)
                    }
                    output.flush()
                }
            }
            return tempFile
        }
        return null
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                startGallery()
            }
        }

    private fun readExternalStorageGranted() = READ_STORAGE_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun writeExternalStorageGranted() = WRITE_STORAGE_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            if (selectedImg != null) {
                val inputStream = this.contentResolver.openInputStream(selectedImg)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val myFile = uriToFile(selectedImg, this)
                getFile = myFile
                layout_img_preview.visibility = View.VISIBLE
                img_preview.setImageBitmap(bitmap)
            } else {
                getFile = null
                layout_img_preview.visibility = View.GONE
                img_preview.setImageDrawable(null)
            }
        }
    }
    private fun saveNote(){
        val image = getFile?.toString() ?: ""
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
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch{

                var notes = Notes()
                notes.title=notes_title.text.toString()
                notes.subTitle=notes_sub_title.text.toString()
                notes.noteText=notes_desc.text.toString()
                notes.dateTime=currentDate
                notes.color=color
                notes.imgPath=image
                notes.webLink=webLink
                notes.favorite=fav
                applicationContext?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    notes_title.setText("")
                    notes_sub_title.setText("")
                    notes_desc.setText("")
                    favButton.setImageResource(R.drawable.favoriteoff)
                    fav=false
                    getFile=null
                    layout_img_preview.visibility=View.GONE
                    img_preview.setImageDrawable(null)
                }
            }
            Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()


        }

    }
}