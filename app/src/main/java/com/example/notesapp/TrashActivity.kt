package com.example.notesapp

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.TrashAdapter
import com.example.notesapp.database.TrashDatabase
import com.example.notesapp.entities.Trash
import kotlinx.android.synthetic.main.activity_trash.*
import kotlinx.android.synthetic.main.activity_trash.backButton
import kotlinx.android.synthetic.main.delete_permi_dialog.view.*
import kotlinx.android.synthetic.main.delete_trash_permi_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TrashActivity : AppCompatActivity() {
    var arrTrash = ArrayList<Trash>()
    var trashAdapter: TrashAdapter = TrashAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)
        backButton.setOnClickListener {
            NoteFragment().updateRecyclerView()
            finish()
        }
        allTrashDelete.setOnClickListener {
            val view = View.inflate(this, R.layout.delete_trash_permi_dialog, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            view.cancel_trashdelete_permi.setOnClickListener {
                dialog.dismiss()
            }
            view.yes_trashdelete_permi.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    TrashDatabase.getDatabase(this@TrashActivity).trashDao().deleteNote(arrTrash)
                    trashAdapter.clearData() // Tüm verileri temizler
                    dialog.dismiss() // Dialog penceresini kapat
                }
            }
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
        recycler_view_trash.setHasFixedSize(true)
        recycler_view_trash.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        GlobalScope.launch(Dispatchers.Main){
            let {
                var notes = TrashDatabase.getDatabase(this@TrashActivity).trashDao().getAllTrash()
                trashAdapter!!.setData(notes)
                arrTrash = notes as ArrayList<Trash>
                val arrNotes = notes.toMutableList()
                trashAdapter!!.setData(arrNotes)
                recycler_view_trash.adapter = trashAdapter
            }

        }
        searchTrash.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Trash>()

                for (arr in arrTrash){
                    if (arr.title_t!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                trashAdapter.setData(tempArr)
                trashAdapter.notifyDataSetChanged()
                return true
            }

        })

    }


}