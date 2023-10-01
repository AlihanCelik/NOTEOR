package com.example.notesapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class NoteFragment : Fragment() {
    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter(0)
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sortType: String

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    companion object {
        fun newInstance(): NoteFragment {
            return NoteFragment()
        }
        private const val SORT_TYPE_KEY = "sortType"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

            GlobalScope.launch(Dispatchers.Main){
                context?.let {
                    var notes: List<Notes> = emptyList()
                    if (sortType=="modifiedTime"){
                        notes = NotesDatabase.getDatabase(it).noteDao().getAllNotesSortedByDate().asReversed()
                    }else{
                        notes = NotesDatabase.getDatabase(it).noteDao().getAllNotesCreatedSortedByDate().asReversed()
                    }

                    val arrNotes = notes.toMutableList()
                    notesAdapter!!.setData(arrNotes)
                    recycler_view.adapter = notesAdapter
                }

            }


        sortButton.setOnClickListener {
            val bottomSheet =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(context).inflate(
                R.layout.bottom_sheet_short,null
            ) as ConstraintLayout
            if(sortType=="modifiedTime"){
                bottomSheetView.findViewById<View>(R.id.modifed_done).visibility=View.VISIBLE
                bottomSheetView.findViewById<View>(R.id.created_done).visibility=View.GONE
            }else{
                bottomSheetView.findViewById<View>(R.id.modifed_done).visibility=View.GONE
                bottomSheetView.findViewById<View>(R.id.created_done).visibility=View.VISIBLE
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.modified_sort).setOnClickListener {
                bottomSheetView.findViewById<View>(R.id.modifed_done).visibility=View.VISIBLE
                bottomSheetView.findViewById<View>(R.id.created_done).visibility=View.GONE
                updateSortType("modifiedTime")

            }
            bottomSheetView.findViewById<LinearLayout>(R.id.created_sort).setOnClickListener {
                bottomSheetView.findViewById<View>(R.id.modifed_done).visibility=View.GONE
                bottomSheetView.findViewById<View>(R.id.created_done).visibility=View.VISIBLE
                updateSortType("createdTime")

            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

        search.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Notes>()

                for (arr in arrNotes){
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }

        })
    }

    fun updateSortType(newSortType: String) {
        sortType = newSortType
        sharedPreferences.edit().putString(SORT_TYPE_KEY, sortType).apply()
        updateRecyclerView()
    }

    fun updateRecyclerView() {
        if(sortType=="modifiedTime"){
            GlobalScope.launch(Dispatchers.Main) {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotesSortedByDate().asReversed()
                    notesAdapter.updateData(notes)
                }
            }
        }else{
            GlobalScope.launch(Dispatchers.Main) {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotesCreatedSortedByDate().asReversed()
                    notesAdapter.updateData(notes)
                }
            }
        }
    }



}