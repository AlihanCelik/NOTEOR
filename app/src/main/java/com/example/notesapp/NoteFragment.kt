package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.Adapter.sortCategoryAdapter
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Category
import com.example.notesapp.entities.Notes
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class NoteFragment : Fragment(), sortCategoryAdapter.SortCategoryClickListener{
    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter(0)

    lateinit var sharedPreferences: SharedPreferences
    lateinit var sortType: String
    var sortCategory: Int=1
    lateinit var categoryAdapter:sortCategoryAdapter
    override fun onResume() {
        super.onResume()
        loadNotesByCategoryId()
    }

    companion object {
        fun newInstance(): NoteFragment {
            return NoteFragment()
        }
        private const val SORT_TYPE_KEY = "sortType"
        private const val SORT_TYPE_KEY2 = "sortCategory"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"
        sortCategory = sharedPreferences.getInt(SORT_TYPE_KEY2, 1)
        categoryAdapter = sortCategoryAdapter(this, sortCategory)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryAdapter = sortCategoryAdapter(this, sortCategory)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        lifecycleScope.launch(Dispatchers.Main) {
            context?.let {
                if (sortCategory == 1) {
                    val sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"
                    val notes = if (sortType == "modifiedTime") {
                        NotesDatabase.getDatabase(it).noteDao().getAllNotesSortedByDate().asReversed()
                    } else {
                        NotesDatabase.getDatabase(it).noteDao().getAllNotesCreatedSortedByDate().asReversed()
                    }
                    notesAdapter!!.setData(notes)
                } else {
                    val sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"
                    val notes = NotesDatabase.getDatabase(it).noteDao().getNotesByCategoryIdSorted(sortCategory, sortType).asReversed()
                    notesAdapter.setData(notes)
                }
                recycler_view.adapter = notesAdapter
            }
        }

        createButton.setOnClickListener {
            val bottomSheet =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                R.layout.bottom_sheet_create,null
            ) as ConstraintLayout
            bottomSheetView.findViewById<LinearLayout>(R.id.writing_create).setOnClickListener {
                val intent= Intent(requireContext(),CreateNoteActivity::class.java)
                startActivity(intent)
                bottomSheet.dismiss()
            }
            bottomSheetView.findViewById<LinearLayout>(R.id.list_create).setOnClickListener {
                val intent= Intent(requireContext(),CreateListActivity::class.java)
                startActivity(intent)
                bottomSheet.dismiss()
            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

        sortButton.setOnClickListener {
            val bottomSheet =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(context).inflate(
                R.layout.bottom_sheet_short,null
            ) as ConstraintLayout
            var recyclerView_ct_sort=bottomSheetView.findViewById<RecyclerView>(R.id.bottom_sheet_category_sort)
            recyclerView_ct_sort.setHasFixedSize(true)
            recyclerView_ct_sort.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
           lifecycleScope.launch(Dispatchers.Main){
                context?.let {
                    var category: List<Category> = emptyList()
                        category = CategoryDatabase.getDatabase(it).CategoryDao().getAllCategory()
                    val arrCategory =category.toMutableList()
                    categoryAdapter!!.setData(arrCategory)
                    recyclerView_ct_sort.adapter = categoryAdapter
                }

            }
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
            bottomSheetView.findViewById<ImageView>(R.id.cancel_bottom_button).setOnClickListener {
                bottomSheet.dismiss()
            }
            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.setCancelable(false)
            bottomSheet.setCanceledOnTouchOutside(true)
            bottomSheet.show()
        }

        search.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<Notes>()
                var tempArr2=ArrayList<Notes>()
                for (arr in arrNotes){
                    if(arr.noteCategoryId==sortCategory&& sortCategory!=1){
                        if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                            tempArr.add(arr)
                        }
                    }
                    if(sortCategory==1){
                        if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                            tempArr2.add(arr)
                        }
                    }
                }
                if(sortCategory==1){
                    notesAdapter.setData(tempArr2)
                }else{
                    notesAdapter.setData(tempArr)
                }
                notesAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    fun updateSortType(newSortType: String) {
        sortType = newSortType
        sharedPreferences.edit().putString(SORT_TYPE_KEY, sortType).apply()
        loadNotesByCategoryId()
    }
    fun updateSortCategory(newSortCategory: Int) {
        sortCategory = newSortCategory
        sharedPreferences.edit().putInt(SORT_TYPE_KEY2, sortCategory).apply()
        loadNotesByCategoryId()
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

    override fun onSortCategoryClick(category: Category) {
        updateSortCategory(category.id_category!!)
        loadNotesByCategoryId()
    }

    fun loadNotesByCategoryId() {
        lifecycleScope.launch(Dispatchers.Main) {
            context?.let {
                if (sortCategory == 1) {
                    val sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"
                    val notes = if (sortType == "modifiedTime") {
                        NotesDatabase.getDatabase(it).noteDao().getAllNotesSortedByDate().asReversed()
                    } else {
                        NotesDatabase.getDatabase(it).noteDao().getAllNotesCreatedSortedByDate().asReversed()
                    }
                    arrNotes.clear()
                    arrNotes.addAll(notes)
                    notesAdapter.updateData(notes)
                } else {
                    val sortType = sharedPreferences.getString(SORT_TYPE_KEY, "modifiedTime") ?: "modifiedTime"
                    val notes = NotesDatabase.getDatabase(it).noteDao().getNotesByCategoryIdSorted(sortCategory, sortType).asReversed()
                    arrNotes.clear()
                    arrNotes.addAll(notes)
                    notesAdapter.updateData(notes)
                }
            }
            visible()
        }
    }
    fun visible(){
        if(arrNotes.isNullOrEmpty()){
            recycler_view.visibility=View.INVISIBLE
            EmptyNote_layout.visibility=View.VISIBLE
        }else{
            recycler_view.visibility=View.VISIBLE
            EmptyNote_layout.visibility=View.GONE
        }
    }

}