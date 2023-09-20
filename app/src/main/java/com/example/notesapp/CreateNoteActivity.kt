package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Typeface
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
import java.io.File
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.WindowInsetsController
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.ImageAdapter
import com.example.notesapp.Adapter.LinksAdapter
import kotlinx.android.synthetic.main.createactivty_permi_dialog.view.*
import kotlinx.android.synthetic.main.dialog_url.view.*
import kotlinx.android.synthetic.main.font_dialog.view.*
import kotlinx.android.synthetic.main.item_notes.view.*
import kotlinx.android.synthetic.main.locked_dialog.view.*
import kotlinx.android.synthetic.main.password_remove_dialog.view.*
import kotlinx.android.synthetic.main.record_voice_dialog.view.*

class CreateNoteActivity : AppCompatActivity() {
    var currentDate:String? = null
    var getFile: File? = null
    var color="blue"
    var webLink = ""
    var fav=false
    var picLay=true
    var linkLay=true
    var textBold=false
    var password=""
    var passwordBoolean=false
    var PICK_IMAGES_CODE = 1
    var noteId=-1


    private val PERMISSION_CODE = 1001
    private val permissionId=14
    private var permissionList=
        if(Build.VERSION.SDK_INT>=33) {
            arrayListOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    lateinit var items: MutableList<Uri>
    lateinit var recyclerView: RecyclerView
    lateinit var imageAdapter: ImageAdapter
    lateinit var linksAdapter: LinksAdapter
    lateinit var items_link:MutableList<String>

    lateinit var recyclerViewLink: RecyclerView
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
        items = arrayListOf()
        items_link= arrayListOf()
        recyclerView = findViewById(R.id.rv_recyclerView)
        recyclerViewLink=findViewById(R.id.links_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerViewLink.setHasFixedSize(true)
        recyclerView.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewLink.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        noteId = intent.getIntExtra("itemid",-1)


        if(noteId!=-1){
            GlobalScope.launch(Dispatchers.Main){
                let {
                    var notes = NotesDatabase.getDatabase(this@CreateNoteActivity).noteDao().getSpecificNote(noteId)
                    if(notes.favorite == true){
                        fav=true
                        favButton.setImageResource(R.drawable.favoriteon)
                    }else{
                        fav=false
                        favButton.setImageResource(R.drawable.favoriteoff)
                    }

                    items= notes.imgPath as MutableList<Uri>
                    if(items.isNotEmpty()){
                        layout_img_preview.visibility = View.VISIBLE
                    }
                    items_link= notes.webLink as MutableList<String>
                    if(items_link.isNotEmpty()){
                        layout_link_preview.visibility = View.VISIBLE
                    }
                    password= notes.password.toString()
                    notes_title.setText(notes.title)
                    notes_sub_title.setText(notes.subTitle)
                    notes_desc.setText(notes.noteText)
                    when (notes.color) {
                        "blue" -> {
                            color = "blue"
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
                        "pink" -> {
                            color = "pink"
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
                        "purple" -> {
                            color = "purple"
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
                        "yellow" -> {
                            color = "yellow"
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
                        "green" -> {
                            color = "green"
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
                        "red" -> {
                            color = "red"
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
                        "orange" -> {
                            color = "orange"
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

                    }
                    initAdapter()
                }
            }
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
            GlobalScope.launch(Dispatchers.Main){
                if (noteId != -1) {
                    if (isDifferent()) {
                        val view = View.inflate(this@CreateNoteActivity, R.layout.createactivty_permi_dialog, null)
                        val builder = AlertDialog.Builder(this@CreateNoteActivity)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        view.permi_text.text = "Are you sure you want to exit without saving changes?"
                        view.cancel_permi.setOnClickListener {
                            dialog.dismiss()
                        }
                        view.yes_permi.setOnClickListener {
                            dialog.dismiss()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    } else {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    if (fav || !items.isEmpty() || !items_link.isEmpty() || password != "" ||
                        !notes_title.text.toString().isNullOrEmpty() ||
                        !notes_sub_title.text.toString().isNullOrEmpty() ||
                        !notes_desc.text.toString().isNullOrEmpty()
                    ) {
                        val view = View.inflate(this@CreateNoteActivity, R.layout.createactivty_permi_dialog, null)
                        val builder = AlertDialog.Builder(this@CreateNoteActivity)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        view.permi_text.text = "Are you sure you want to exit without saving?"
                        view.cancel_permi.setOnClickListener {
                            dialog.dismiss()
                        }
                        view.yes_permi.setOnClickListener {
                            dialog.dismiss()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    } else {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
        saveButton.setOnClickListener {
            saveNote()
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
            if (passwordBoolean) {
                bottomSheetView.findViewById<ImageView>(R.id.locked).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.unlocked))
            } else {
                bottomSheetView.findViewById<ImageView>(R.id.locked).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.locked))
            }
            bottomSheetView.findViewById<View>(R.id.blue).setOnClickListener {
                color = "blue"
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
                color = "pink"
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
                color = "purple"
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
                color = "green"
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
                color = "yellow"
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
                color = "red"
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
                color = "orange"
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
                if (hasPermissions()) {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Image(s)"),
                        PICK_IMAGES_CODE
                    )
                    bottomSheet.dismiss()
                } else {
                    requestPermissions()
                }

            }
            bottomSheetView.findViewById<View>(R.id.link).setOnClickListener {
                if (!isFinishing) {
                    val view = View.inflate(this, R.layout.dialog_url, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view.okeyUrl.setOnClickListener {
                        if (view.etWebLink.text.toString() != "") {
                            webLink = view.etWebLink.text.toString()
                            items_link.add(webLink)
                            linksAdapter.notifyDataSetChanged()
                            layout_link_preview.visibility = View.VISIBLE
                        }

                        dialog.dismiss()
                    }
                }
                bottomSheet.dismiss()

            }

            bottomSheetView.findViewById<View>(R.id.locked).setOnClickListener {
                if (!isFinishing) {
                    val view = View.inflate(this, R.layout.locked_dialog, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(view)
                    val dialog = builder.create()

                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    if (passwordBoolean) {

                            if (!isFinishing) {
                                val view = View.inflate(this, R.layout.password_remove_dialog, null)
                                val builder = AlertDialog.Builder(this)
                                builder.setView(view)
                                val dialog = builder.create()
                                dialog.show()
                                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                view.okRemovePsw.setOnClickListener {
                                    password = ""
                                    passwordBoolean = false
                                    dialog.dismiss()
                                }
                                view.cancelRemovePsw.setOnClickListener{
                                    dialog.dismiss()
                                }
                            }
                            bottomSheet.dismiss()



                    } else {
                        dialog.show()
                        var confirm_password = ""
                        view.okeylock.setOnClickListener {
                            if (view.confirmpasswordContainer.helperText == "Successful" &&
                                view.passwordContainer.helperText == "Successful" &&
                                view.confirm_passwordEditText.text.toString() == view.passwordEditText.text.toString()
                            ) {
                                password = view.confirm_passwordEditText.text.toString()
                                passwordBoolean = true
                                dialog.dismiss()
                            } else {
                                view.confirmpasswordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this@CreateNoteActivity,
                                            android.R.color.holo_red_dark
                                        )
                                    )
                                )
                                view.confirmpasswordContainer.helperText = "Enter Confirm Password"

                                view.passwordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this@CreateNoteActivity,
                                            android.R.color.holo_red_dark
                                        )
                                    )
                                )
                                view.passwordContainer.helperText = "Enter Password"
                            }
                        }
                        view.passwordEditText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                view.passwordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this@CreateNoteActivity,
                                            android.R.color.holo_green_dark
                                        )
                                    )
                                )
                                view.passwordContainer.helperText = "Enter Password"
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                var password = s.toString()
                                if (password.length < 4) {
                                    view.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@CreateNoteActivity,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view.passwordContainer.helperText =
                                        "Minimum 4 Character Password"
                                    view.passwordContainer.error = ""
                                } else if (password.length in 4..10) {
                                    view.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@CreateNoteActivity,
                                                android.R.color.holo_green_dark
                                            )
                                        )
                                    )

                                    view.passwordContainer.helperText = "Successful"
                                    passwordBoolean = true
                                } else {
                                    view.passwordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@CreateNoteActivity,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view.passwordContainer.helperText =
                                        "Maximum 10 Character Password"
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {
                                confirm_password = view.passwordEditText.text.toString()
                            }
                        })

                        view.confirm_passwordEditText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                view.confirmpasswordContainer.setHelperTextColor(
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this@CreateNoteActivity,
                                            android.R.color.holo_green_dark
                                        )
                                    )
                                )
                                view.confirmpasswordContainer.helperText = "Enter Confirm Password"
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (confirm_password != view.confirm_passwordEditText.text.toString() ||
                                    view.passwordContainer.helperText != "Successful"
                                ) {
                                    view.confirmpasswordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@CreateNoteActivity,
                                                android.R.color.holo_red_dark
                                            )
                                        )
                                    )
                                    view.confirmpasswordContainer.helperText =
                                        "Must match the previous entry"
                                    view.confirmpasswordContainer.error = ""
                                } else {
                                    view.confirmpasswordContainer.setHelperTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@CreateNoteActivity,
                                                android.R.color.holo_green_dark
                                            )
                                        )
                                    )
                                    view.confirmpasswordContainer.helperText = "Successful"
                                    view.confirmpasswordContainer.error = ""
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })
                    }
                    bottomSheet.dismiss()
                }
            }
            bottomSheetView.findViewById<View>(R.id.mic).setOnClickListener {
                if (!isFinishing) {
                    val view = View.inflate(this, R.layout.record_voice_dialog, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view.okeyMic.setOnClickListener {
                        dialog.dismiss()
                    }

                    bottomSheet.dismiss()
                }

            }

            bottomSheetView.findViewById<View>(R.id.font).setOnClickListener {

                if (!isFinishing) {
                    val view = View.inflate(this, R.layout.font_dialog, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view.okeyFont.setOnClickListener {
                        dialog.dismiss()
                    }
                    if (textBold) {
                        view.font_bold_btn.setColorFilter(resources.getColor(R.color.blue1))
                    } else {
                        view.font_bold_btn.setColorFilter(resources.getColor(R.color.darkGrey))
                    }
                    view.font_bold_btn.setOnClickListener {
                        var currentText = notes_desc.text.toString()

                        if (textBold) {
                            val spannable = SpannableStringBuilder(currentText)
                            var selectionStart = notes_desc.text.toString().length
                            var selectionEnd = notes_desc.selectionEnd
                            spannable.setSpan(
                                StyleSpan(Typeface.NORMAL),
                                selectionStart,
                                selectionEnd,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            view.font_bold_btn.setColorFilter(resources.getColor(R.color.darkGrey))
                            textBold = !textBold
                            notes_desc.text = spannable
                        } else {
                            var selectionStart = notes_desc.text.toString().length
                            var selectionEnd = notes_desc.selectionEnd
                            val spannable = SpannableStringBuilder(currentText)
                            spannable.setSpan(
                                StyleSpan(Typeface.BOLD),
                                selectionStart,
                                selectionEnd,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            view.font_bold_btn.setColorFilter(resources.getColor(R.color.blue1))
                            textBold = !textBold
                            notes_desc.text = spannable
                        }
                    }
                    view.font1_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.ralewaymedium),
                            Typeface.NORMAL
                        )
                    }
                    view.font2_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font4),
                            Typeface.NORMAL
                        )
                    }
                    view.font3_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font3),
                            Typeface.NORMAL
                        )
                    }
                    view.font4_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font4),
                            Typeface.NORMAL
                        )
                    }
                    view.font5_btn.setOnClickListener {
                        notes_desc.textSize = 20f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font5),
                            Typeface.NORMAL
                        )
                    }
                    view.font6_btn.setOnClickListener {
                        notes_desc.textSize = 22f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font6),
                            Typeface.NORMAL
                        )
                    }
                    view.font7_btn.setOnClickListener {
                        notes_desc.textSize = 22f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font7),
                            Typeface.NORMAL
                        )
                    }
                    view.font8_btn.setOnClickListener {
                        notes_desc.textSize = 22f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font8),
                            Typeface.NORMAL
                        )
                    }
                    view.font9_btn.setOnClickListener {
                        notes_desc.textSize = 20f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font9),
                            Typeface.NORMAL
                        )
                    }
                    view.font10_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font10),
                            Typeface.NORMAL
                        )
                    }
                    view.font11_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font11),
                            Typeface.NORMAL
                        )
                    }
                    view.font12_btn.setOnClickListener {
                        notes_desc.textSize = 17f
                        notes_desc.setTypeface(
                            ResourcesCompat.getFont(this, R.font.font12),
                            Typeface.NORMAL
                        )
                    }
                    bottomSheet.dismiss()
                }


            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

        pictures_layout.setOnClickListener {
            if(picLay){
                picLay=false
                rv_recyclerView.visibility=View.GONE
                pictures_updown.setImageResource(R.drawable.arrowdown)
            }else{
                picLay=true
                rv_recyclerView.visibility=View.VISIBLE
                pictures_updown.setImageResource(R.drawable.uparrow)
            }

        }

        link_layout.setOnClickListener {
            if(linkLay==true){
                linkLay=false
                links_recyclerView.visibility=View.GONE
                link_updown.setImageResource(R.drawable.arrowdown)
            }else{
                linkLay=true
                links_recyclerView.visibility=View.VISIBLE
                link_updown.setImageResource(R.drawable.uparrow)
            }

        }


        initAdapter()

    }
    private suspend fun isDifferent(): Boolean = coroutineScope {
        val notes = async(Dispatchers.IO) {
            NotesDatabase.getDatabase(this@CreateNoteActivity).noteDao().getSpecificNote(noteId)
        }

        val itemFav = notes.await().favorite == true
        val itemPictures = notes.await().imgPath as MutableList<Uri>
        val itemLinks = notes.await().webLink as MutableList<String>
        val itemPsw = notes.await().password.toString()
        val itemTitle = notes.await().title.toString()
        val itemSubTitle = notes.await().subTitle.toString()
        val itemDesc = notes.await().noteText.toString()

        return@coroutineScope (fav != itemFav || !itemsEquals(itemPictures, items) || !itemsEquals(itemLinks, items_link)
                || password != itemPsw || notes_title.text.toString() != itemTitle ||
                notes_sub_title.text.toString() != itemSubTitle || notes_desc.text.toString() != itemDesc)
    }
    private fun itemsEquals(list1: List<*>, list2: List<*>): Boolean {
        if (list1.size != list2.size) {
            return false
        }

        for (i in list1.indices) {
            if (list1[i] != list2[i]) {
                return false
            }
        }

        return true
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
                // İzinler verildiyse, işlemleri gerçekleştir
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image(s)"),
                    PICK_IMAGES_CODE
                )
            } else {
                // İzinler reddedildiyse, kullanıcıya bir mesaj gösterin veya gerekli işlemleri yapın
                // Örneğin, izinler reddedildiğinde bir açıklama gösterebilirsiniz.
            }
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
            if(noteId!=-1){
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                coroutineScope.launch {
                    applicationContext?.let{
                        var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                        notes.title=notes_title.text.toString()
                        notes.subTitle=notes_sub_title.text.toString()
                        notes.noteText=notes_desc.text.toString()
                        notes.dateTime=currentDate
                        notes.color=color
                        notes.imgPath=items
                        notes.webLink=items_link
                        notes.favorite=fav
                        notes.password=password
                        NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                        setResult(Activity.RESULT_OK)
                    }

                }

            }else{
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                coroutineScope.launch{
                    var notes = Notes()
                    notes.title=notes_title.text.toString()
                    notes.subTitle=notes_sub_title.text.toString()
                    notes.noteText=notes_desc.text.toString()
                    notes.dateTime=currentDate
                    notes.color=color
                    notes.imgPath=items
                    notes.webLink=items_link
                    notes.favorite=fav
                    notes.password=password
                    applicationContext?.let {

                        NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)

                        notes_title.setText("")
                        notes_sub_title.setText("")
                        notes_desc.setText("")
                        favButton.setImageResource(R.drawable.favoriteoff)
                        fav=false
                        getFile=null
                        layout_img_preview.visibility=View.GONE
                        layout_link_preview.visibility=View.GONE
                        items.clear()
                        items_link.clear()
                        password=""
                        passwordBoolean=false
                        setResult(Activity.RESULT_OK)
                    }


                }
            }


            Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && data.clipData != null) {

            val count = data.clipData!!.itemCount
            for (i in 0 until count) {
                val imageUri = data.clipData!!.getItemAt(i).uri
                items.add(imageUri)
                imageAdapter.notifyDataSetChanged()
                layout_img_preview.visibility = View.VISIBLE

            }

        }

    }

    private fun initAdapter() {
        imageAdapter = ImageAdapter(this, items,layout_img_preview)
        val ll = GridLayoutManager(this, 2)
        recyclerView.layoutManager = ll
        recyclerView.adapter = imageAdapter

        linksAdapter= LinksAdapter(this,items_link,layout_link_preview)
        recyclerViewLink.adapter=linksAdapter



    }




}



