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
import java.io.FileOutputStream
import java.io.IOException

class CreateNoteActivity : AppCompatActivity() {
    var currentDate:String? = null
    private var getFile: File? = null
    var color="blue"
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
        btn_clear_added_img.setOnClickListener {
            getFile = null
            layout_img_preview.visibility = View.GONE
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
            bottomSheetView.findViewById<View>(R.id.image).setOnClickListener {
                startGallery()
                bottomSheet.dismiss()

            }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

    }
    fun uriToFile(uri: Uri, context: Context): File? {
        val file = File(context.cacheDir, "temp_file")
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
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
            val inputStream = this.contentResolver.openInputStream(selectedImg!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            layout_img_preview.visibility = View.VISIBLE
            img_preview.setImageBitmap(bitmap)
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
                notes.imgPath=getFile.toString()
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