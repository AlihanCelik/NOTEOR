package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class NoteFragment : Fragment() {
    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter()

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    companion object {
        fun newInstance(): NoteFragment {
            return NoteFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotesSortedByDate().asReversed()

                val arrNotes = notes.toMutableList()
                notesAdapter!!.setData(arrNotes)
                recycler_view.adapter = notesAdapter
            }

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

    fun updateRecyclerView() {
        GlobalScope.launch(Dispatchers.Main) {
            context?.let {
                val notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                notesAdapter.updateData(notes)
            }
        }
    }


}