package com.example.notesapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Paint

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.CategoryAdapter
import com.example.notesapp.adThen.ImageAdapter
import com.example.notesapp.Adapter.LinksAdapter
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Category
import com.example.notesapp.entities.Notes
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_create_note.backButton
import kotlinx.android.synthetic.main.activity_create_note.createNote
import kotlinx.android.synthetic.main.createactivty_permi_dialog.view.*
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.dialog_url.view.*
import kotlinx.android.synthetic.main.locked_dialog.view.*
import kotlinx.android.synthetic.main.password_remove_dialog.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

import kotlinx.coroutines.suspendCancellableCoroutine

import androidx.lifecycle.lifecycleScope
import com.example.notesapp.database.TrashDatabase
import com.example.notesapp.entities.Trash
import kotlinx.android.synthetic.main.activity_create_list.*
import kotlinx.android.synthetic.main.activity_create_note.category_button
import kotlinx.android.synthetic.main.activity_create_note.category_name
import kotlinx.android.synthetic.main.activity_create_note.category_updownarrow
import kotlinx.android.synthetic.main.activity_create_note.colorView
import kotlinx.android.synthetic.main.activity_create_note.editNote
import kotlinx.android.synthetic.main.activity_create_note.favButton
import kotlinx.android.synthetic.main.activity_create_note.more
import kotlinx.android.synthetic.main.activity_create_note.notes_sub_title
import kotlinx.android.synthetic.main.activity_create_note.notes_title
import kotlinx.android.synthetic.main.activity_create_note.readNote
import kotlinx.android.synthetic.main.activity_create_note.reminderlayout
import kotlinx.android.synthetic.main.activity_create_note.saveButton
import kotlinx.android.synthetic.main.activity_create_note.tvDateTime
import kotlinx.android.synthetic.main.activity_create_note.tvReminderTime
import kotlinx.android.synthetic.main.bottom_sheet_note.*
import kotlinx.android.synthetic.main.delete_permi_dialog.view.*


class CreateNoteActivity : AppCompatActivity(),CategoryAdapter.CategoryClickListener {
    var currentDate:String? = null
    var color="blue"
    var webLink = ""
    var fav=false
    var picLay=true
    var linkLay=true
    var password=""
    var passwordBoolean=false
    var PICK_IMAGES_CODE = 1002
    var noteId=-1
    var categoryName=-1
    var reminder: Long? =null

    private val PERMISSION_CODE = 1001
    private val permissionId=14
    private var permissionList=
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
            arrayListOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO)
        }

    lateinit var items: MutableList<Uri>
    lateinit var recyclerView: RecyclerView
    lateinit var imageAdapter: ImageAdapter
    lateinit var linksAdapter: LinksAdapter
    lateinit var categoryAdapter: CategoryAdapter
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
        val sdf =SimpleDateFormat("yyyy-MM-dd HH:mm")

        val backgroundYellow=resources.getColor(R.color.background_yellow)
        val backgroundRed=resources.getColor(R.color.background_red)
        val backgroundBlue=resources.getColor(R.color.background_blue)
        val backgroundGreen=resources.getColor(R.color.background_green)
        val backgroundPink=resources.getColor(R.color.background_pink)
        val backgroundPurple=resources.getColor(R.color.background_purple)
        val backgroundOrange=resources.getColor(R.color.background_orange)
        val sharedPreferences = getSharedPreferences("NoteorPrefs", Context.MODE_PRIVATE)
        color = sharedPreferences.getString("selectedColor", "blue") ?: "blue"
        currentDate=sdf.format(Date())
        createNotificationChannel()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        items = arrayListOf()
        items_link= arrayListOf()
        recyclerView = findViewById(R.id.rv_recyclerView)
        recyclerViewLink=findViewById(R.id.links_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerViewLink.setHasFixedSize(true)
        recyclerView.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewLink.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        noteId = intent.getIntExtra("itemid",-1)
        tvDateTime.text=currentDate

        when (color) {
            "blue" -> {
                setThemeColors(moonBlue,backgroundBlue,moonBlue)
            }
            "pink" -> {
                setThemeColors(moonPink,backgroundPink,moonPink)

            }
            "purple" -> {
                setThemeColors(moonPurple,backgroundPurple,moonPurple)

            }
            "yellow" -> {
                setThemeColors(moonYellow,backgroundYellow,moonYellow)

            }
            "green" -> {
                setThemeColors(moonGreen,backgroundGreen,moonGreen)

            }
            "red" -> {
                setThemeColors(moonRed,backgroundRed,moonRed)

            }
            "orange" -> {
                setThemeColors(moonOrange,backgroundOrange,moonOrange)
            }

        }




        if(noteId!=-1){
            readNote.visibility=View.GONE
            editNote.visibility=View.VISIBLE
            more.visibility=View.GONE
            category_updownarrow.visibility=View.GONE
            favButton.visibility=View.GONE
            saveButton.visibility=View.GONE
            notes_desc.isEnabled = false
            notes_title.isEnabled=false
            notes_sub_title.isEnabled=false
            category_button.isEnabled=false
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
                    if(notes.noteCategoryId!=-1){
                        categoryName= notes.noteCategoryId!!
                        GlobalScope.launch(Dispatchers.Main){
                            let {
                                var category=CategoryDatabase.getDatabase(this@CreateNoteActivity).CategoryDao().getAllCategory()
                                var arrCategory = category as ArrayList<Category>
                                for (arr in arrCategory){
                                    if(arr.id_category==categoryName){
                                        category_name.text=arr.name_category.toString()
                                    }
                                }
                            }

                        }
                    }else{
                        category_name.text="Categories"
                    }

                    if(notes.reminder!=null){
                        reminderlayout.visibility=View.VISIBLE
                        val reminderDateItem = Date(notes.reminder!!)
                        val formattedDateItem = sdf.format(reminderDateItem)
                        tvReminderTime.text=formattedDateItem
                        reminder=notes.reminder
                        if(notes.reminder!! <System.currentTimeMillis()){
                            tvReminderTime.paintFlags =Paint.STRIKE_THRU_TEXT_FLAG
                        }else{
                            tvReminderTime.paintFlags =0
                        }
                    }else{
                        reminderlayout.visibility=View.GONE
                    }
                    /*
                    items= notes.imgPath as MutableList<Uri>
                    if(items.isNotEmpty()){
                        layout_img_preview.visibility = View.VISIBLE
                    }*/
                    items_link= notes.webLink as MutableList<String>
                    if(items_link.isNotEmpty()){
                        layout_link_preview.visibility = View.VISIBLE
                    }
                    password= notes.password.toString()
                    if(!password.isNullOrEmpty()){
                        passwordBoolean=true
                    }
                    notes_title.setText(notes.title)
                    notes_sub_title.setText(notes.subTitle)
                    notes_desc.setText(notes.noteText)
                    tvDateTime.text=notes.dateTime

                    color=notes.color.toString()
                    when (color) {
                        "blue" -> {
                            setThemeColors(moonBlue,backgroundBlue,moonBlue)
                        }
                        "pink" -> {
                            setThemeColors(moonPink,backgroundPink,moonPink)

                        }
                        "purple" -> {
                            setThemeColors(moonPurple,backgroundPurple,moonPurple)

                        }
                        "yellow" -> {
                            setThemeColors(moonYellow,backgroundYellow,moonYellow)

                        }
                        "green" -> {
                            setThemeColors(moonGreen,backgroundGreen,moonGreen)

                        }
                        "red" -> {
                            setThemeColors(moonRed,backgroundRed,moonRed)

                        }
                        "orange" -> {
                            setThemeColors(moonOrange,backgroundOrange,moonOrange)
                        }

                    }
                    initAdapter()
                }
            }
        }

        editNote.setOnClickListener {
            readNote.visibility=View.VISIBLE
            notes_desc.isEnabled = true
            notes_title.isEnabled=true
            notes_sub_title.isEnabled=true
            category_button.isEnabled=true
            editNote.visibility=View.GONE
            more.visibility=View.VISIBLE
            category_updownarrow.visibility=View.VISIBLE
            favButton.visibility=View.VISIBLE
            saveButton.visibility=View.VISIBLE
        }
        readNote.setOnClickListener {
            readNote.visibility=View.GONE
            editNote.visibility=View.VISIBLE
            more.visibility=View.GONE
            category_updownarrow.visibility=View.GONE
            favButton.visibility=View.GONE
            saveButton.visibility=View.GONE
            notes_desc.isEnabled = false
            notes_title.isEnabled=false
            notes_sub_title.isEnabled=false
            category_button.isEnabled=false
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
        category_button.setOnClickListener {
            category_updownarrow.setImageResource(R.drawable.uparrow)
            val bottomSheet =
                BottomSheetDialog(this@CreateNoteActivity, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_category,
                findViewById(R.id.bottomSheet_category)
            ) as ConstraintLayout
            bottomSheet.setOnCancelListener {
                category_updownarrow.setImageResource(R.drawable.arrowdown)
            }
            categoryAdapter = CategoryAdapter(this@CreateNoteActivity,categoryName)
            val recv_category=bottomSheetView.findViewById<RecyclerView>(R.id.recycler_view_categorybottom)
            recv_category.setHasFixedSize(true)
            recv_category.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            GlobalScope.launch(Dispatchers.Main) {
                let {
                    var category =
                        CategoryDatabase.getDatabase(this@CreateNoteActivity).CategoryDao()
                            .getAllCategory()
                    categoryAdapter.setData(category)
                    recv_category.adapter = categoryAdapter
                }
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.bottom_add_category).setOnClickListener {
                val view = View.inflate(this, R.layout.dialog_add_category, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                view.okeyCategory.setOnClickListener {
                    val newCategoryName = view.category_name.text.toString()

                    val coroutineScope = CoroutineScope(Dispatchers.Main)
                    coroutineScope.launch {
                        val existingCategories =
                            CategoryDatabase.getDatabase(this@CreateNoteActivity).CategoryDao().getAllCategory()

                        val isCategoryExists = existingCategories.any { it.name_category == newCategoryName }

                        if (newCategoryName.isNotEmpty() && !isCategoryExists) {
                            var category = Category()
                            category.name_category = newCategoryName
                            category.order_category=existingCategories.size
                            applicationContext?.let {
                                val insertedCategoryId =
                                    CategoryDatabase.getDatabase(it).CategoryDao().insertCategory(category)
                                val category2 =
                                    CategoryDatabase.getDatabase(this@CreateNoteActivity).CategoryDao()
                                        .getAllCategory()
                                categoryAdapter.updateData(category2)
                            }
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this@CreateNoteActivity,"This category already exists.", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()


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
            if (passwordBoolean || !password.isNullOrEmpty()) {
                bottomSheetView.findViewById<ImageView>(R.id.locked_img).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.unlocked))
                bottomSheetView.findViewById<TextView>(R.id.locked_text).text="Unlocked"
            } else {
                bottomSheetView.findViewById<ImageView>(R.id.locked_img).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.locked))
                bottomSheetView.findViewById<TextView>(R.id.locked_text).text="Locked"
            }
            if(reminder!=null){
                bottomSheetView.findViewById<ImageView>(R.id.remainder_img).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.reminderonn))
            }else{
                bottomSheetView.findViewById<ImageView>(R.id.remainder_img).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.remindernotes))
            }
            bottomSheetView.findViewById<View>(R.id.blue).setOnClickListener {
                color = "blue"
                colorView.setBackgroundColor(moonBlue)
                createNote.setBackgroundColor(backgroundBlue)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = moonBlue
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
                    window.statusBarColor = moonPink
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
                    window.statusBarColor = moonPurple
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
                    window.statusBarColor = moonGreen
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
                    window.statusBarColor = moonYellow
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
                    window.statusBarColor = moonRed
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
                    window.statusBarColor = moonOrange
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
            /*
            bottomSheetView.findViewById<View>(R.id.mic).setOnClickListener {
                val intent=Intent(applicationContext,VoiceActivity::class.java)
                startActivity(intent)
            }
             */

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
            bottomSheetView.findViewById<View>(R.id.delete).setOnClickListener {
                if(noteId!=-1) {
                    val view3 = View.inflate(this@CreateNoteActivity, R.layout.delete_permi_dialog, null)
                    val builder3 = AlertDialog.Builder(this@CreateNoteActivity)
                    builder3.setView(view3)
                    val dialog3 = builder3.create()
                    dialog3.show()
                    dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view3.cancel_delete_permi.setOnClickListener{
                        dialog3.dismiss()
                    }
                    dialog3.setOnCancelListener {
                        dialog3.dismiss()
                    }
                    view3.yes_delete_permi.setOnClickListener {
                        var notes:Notes
                        var trash:Trash
                        GlobalScope.launch(Dispatchers.Main) {
                                notes = NotesDatabase.getDatabase(this@CreateNoteActivity).noteDao()
                                    .getSpecificNote(noteId)
                                trash = Trash()
                                trash.title_t = notes.title
                                trash.subTitle_t = notes.subTitle
                                trash.noteText_t = notes.noteText
                                trash.dateTime_t = notes.dateTime
                                trash.create_dateTime_t=notes.create_dateTime
                                trash.color_t = notes.color
                                trash.noteCategory_t=notes.noteCategoryId
                                trash.imgPath_t = notes.imgPath
                                trash.webLink_t = notes.webLink
                                trash.favorite_t = notes.favorite
                                trash.password_t = notes.password
                                let {
                                TrashDatabase.getDatabase(this@CreateNoteActivity).trashDao().insertTrash(trash)
                                notes.id?.let { it1 ->
                                    NotesDatabase.getDatabase(this@CreateNoteActivity).noteDao().deleteSpecificNote(
                                        it1
                                    )


                                }
                            }

                        }
                        Toast.makeText(this@CreateNoteActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        dialog3.dismiss()
                        bottomSheet.dismiss()
                        finish()


                    }



                }else{
                    val view3 = View.inflate(this@CreateNoteActivity, R.layout.delete_permi_dialog, null)
                    val builder3 = AlertDialog.Builder(this@CreateNoteActivity)
                    builder3.setView(view3)
                    val dialog3 = builder3.create()
                    dialog3.show()
                    dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    view3.cancel_delete_permi.setOnClickListener{
                        dialog3.dismiss()
                    }
                    dialog3.setOnCancelListener {
                        dialog3.dismiss()
                    }
                    view3.yes_delete_permi.setOnClickListener {
                        dialog3.dismiss()
                        finish()
                        bottomSheet.dismiss()
                    }
                }
            }
            bottomSheetView.findViewById<View>(R.id.share_bs).setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                var text = "${notes_title.text}\n${notes_sub_title.text}\n${notes_desc.text}"

                shareIntent.putExtra(Intent.EXTRA_TEXT, text)
                val chooser = Intent.createChooser(shareIntent, "Share using")
                startActivity(chooser)
            }
            bottomSheetView.findViewById<View>(R.id.remainder).setOnClickListener {
                if(reminder!=null){
                    reminder=null
                    reminderlayout.visibility = View.GONE
                    bottomSheetView.findViewById<ImageView>(R.id.remainder_img).setImageDrawable(ContextCompat.getDrawable(this,R.drawable.remindernotes))
                }else{
                    val calendar = Calendar.getInstance()

                    val datePicker = DatePickerDialog(
                        this,
                        { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            val timePicker = TimePickerDialog(
                                this,
                                { _, hourOfDay, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    val selectedTimeInMillis = calendar.timeInMillis

                                    if (selectedTimeInMillis > System.currentTimeMillis()) {
                                        tvReminderTime.paintFlags =0
                                        reminder = selectedTimeInMillis
                                        val reminderDate = Date(reminder!!)
                                        val formattedDate = sdf.format(reminderDate)
                                        reminderlayout.visibility = View.VISIBLE
                                        tvReminderTime.text = formattedDate
                                        bottomSheetView.findViewById<ImageView>(R.id.remainder_img).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.remindernotes))
                                    } else {
                                        Toast.makeText(this, "Lütfen geçerli bir tarih ve saat seçin", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            )
                            timePicker.show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )

                    datePicker.datePicker.minDate = System.currentTimeMillis()

                    datePicker.show()

                }

            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }
        rv_recyclerView.visibility=View.GONE
        /*pictures_layout.setOnClickListener {
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
        */
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



    override fun onBackPressed() {
        Log.d("CreateNoteActivity", "onBackPressed() called")
       lifecycleScope.launch(Dispatchers.Main) {
            val shouldExit = showExitDialog()
            if (shouldExit) {
                setResult(Activity.RESULT_OK)
                finish()
                println("back")
            }
        }
    }

    private suspend fun showExitDialog(): Boolean {
        return if (noteId == -1) {
            if (fav || !items.isEmpty() || !items_link.isEmpty() || password != "" ||
                !notes_title.text.toString().isNullOrEmpty() ||
                !notes_sub_title.text.toString().isNullOrEmpty() ||
                !notes_desc.text.toString().isNullOrEmpty()) {
                displayExitDialog()
            } else {
                true
            }
        } else {
            val different = isDifferent()
            if (different) {
                displayExitDialog()
            } else {
                true
            }
        }
    }

    private suspend fun displayExitDialog(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val view = View.inflate(this@CreateNoteActivity, R.layout.createactivty_permi_dialog, null)
            val builder = AlertDialog.Builder(this@CreateNoteActivity)
            builder.setView(view)
            val dialog = builder.create()



            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            if (noteId != -1) {
                view.permi_text.text = "Are you sure you want to exit without saving changes?"
            } else {
                view.permi_text.text = "Are you sure you want to exit without saving?"
            }

            view.cancel_permi.setOnClickListener {
                dialog.dismiss()
                continuation.resume(false, null)
            }

            view.yes_permi.setOnClickListener {
                dialog.dismiss()
                continuation.resume(true, null)
            }
        }
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
        val itemColor=notes.await().color.toString()
        return@coroutineScope (fav != itemFav || !itemsEquals(itemPictures, items) || !itemsEquals(itemLinks, items_link)
                || password != itemPsw || notes_title.text.toString() != itemTitle ||
                notes_sub_title.text.toString() != itemSubTitle || notes_desc.text.toString() != itemDesc || color!=itemColor)
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
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image(s)"),
                    PICK_IMAGES_CODE
                )
            } else {

            }
        }
    }
    private fun saveNote(){
        if(notes_title.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Note Title is Required", Toast.LENGTH_SHORT).show()
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
                        notes.create_dateTime=notes.create_dateTime
                        notes.color=color
                        notes.itemList=null
                        notes.reminder=reminder
                        notes.noteCategoryId=categoryName
                        notes.imgPath=items
                        notes.webLink=items_link
                        notes.favorite=fav
                        notes.password=password
                        NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                        setResult(Activity.RESULT_OK)
                        tvDateTime.text=notes.dateTime
                        Toast.makeText(this@CreateNoteActivity, "Note is updated", Toast.LENGTH_SHORT).show()
                        if(reminder!=null && notes.reminder!! >System.currentTimeMillis()){
                            reminder?.let { it1 -> setAlarm(it1,notes.title!!,notes.id!!) }
                        }
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
                    notes.create_dateTime=currentDate
                    notes.color=color
                    notes.itemList=null
                    notes.reminder=reminder
                    notes.noteCategoryId=categoryName
                    notes.imgPath=items
                    notes.webLink=items_link
                    notes.favorite=fav
                    notes.password=password
                    applicationContext?.let {

                        val insertedId = NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                        noteId = insertedId.toInt()
                        setResult(Activity.RESULT_OK)
                        if(reminder!=null && notes.reminder!! >System.currentTimeMillis()){
                            reminder?.let { it1 -> setAlarm(it1,notes.title!!,noteId!!) }
                        }
                        Toast.makeText(this@CreateNoteActivity, "Note is added", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (data != null && data.clipData != null) {

                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    items.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                    layout_img_preview.visibility = View.VISIBLE
                }
            }else if (data != null) {
                val imageUri = data.data
                if (imageUri != null) {
                    items.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                    layout_img_preview.visibility = View.VISIBLE
                }
            }
    }
    */
    private fun initAdapter() {
        imageAdapter = ImageAdapter(this, items,layout_img_preview)
        val ll = GridLayoutManager(this, 2)
        recyclerView.layoutManager = ll
        recyclerView.adapter = imageAdapter

        linksAdapter= LinksAdapter(this,items_link,layout_link_preview)
        recyclerViewLink.adapter=linksAdapter


    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name :CharSequence="ReminderChannel"
            val description="Channel for Alarm Manager"
            val importance= NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel("foxandroid",name,importance)
            channel.description=description
            val notificationManeger=getSystemService(NotificationManager::class.java)
            notificationManeger.createNotificationChannel(channel)
        }
    }
    private fun setAlarm(dateTimeInMillis: Long, noteTitle: String,noteid:Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra("NOTE_TITLE", noteTitle)
        alarmIntent.putExtra("NOTE_ID",noteid)
        alarmIntent.putExtra("list","Note")
        println("Set : $noteid")
        print("Set Title $noteTitle")
        val pendingIntent = PendingIntent.getBroadcast(this, noteid, alarmIntent,  PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeInMillis, pendingIntent)

    }

    override fun onCategoryClick(category: Category) {
        category_name.text=category.name_category
        categoryName= category.id_category!!
    }
    private fun setThemeColors(buttonColor: Int, backgroundColor: Int,moonColor: Int) {
        colorView.setBackgroundColor(buttonColor)
        createNote.setBackgroundColor(backgroundColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = moonColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = backgroundColor
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



