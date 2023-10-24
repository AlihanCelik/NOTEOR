package com.example.notesapp

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.CategoryActivtiyAdapter
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.activity_category_activtiy.*
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CategoryActivtiy : AppCompatActivity() {
    var arrCategory = ArrayList<Category>()
    var categoryAdapter:CategoryActivtiyAdapter= CategoryActivtiyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_activtiy)
        backButton.setOnClickListener {
            finish()
        }
        val itemTouchHelperCallback = ItemTouchHelperCallback(categoryAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view_category)

        category_add.setOnClickListener {
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
                        CategoryDatabase.getDatabase(this@CategoryActivtiy, ).CategoryDao().getAllCategory()

                    val isCategoryExists = existingCategories.any { it.name_category == newCategoryName }

                    if (newCategoryName.isNotEmpty() && !isCategoryExists) {
                        var category = Category()
                        category.name_category = newCategoryName
                        applicationContext?.let {
                            val insertedCategoryId =
                                CategoryDatabase.getDatabase(it).CategoryDao().insertCategory(category)
                            val category2 =
                                CategoryDatabase.getDatabase(this@CategoryActivtiy, ).CategoryDao()
                                    .getAllCategory()
                            categoryAdapter.updateData(category2)
                        }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@CategoryActivtiy,"This category already exists.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
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

        recycler_view_category.setHasFixedSize(true)
        recycler_view_category.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        GlobalScope.launch(Dispatchers.Main){
            let {
                var categories = CategoryDatabase.getDatabase(this@CategoryActivtiy).CategoryDao().getAllCategory()
                categoryAdapter!!.setData(categories)
                arrCategory = categories as ArrayList<Category>
                categoryAdapter.notifyDataSetChanged()
                recycler_view_category.adapter = categoryAdapter
            }

        }
        searchCategories.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Category>()

                for (arr in arrCategory) {
                    if (arr.name_category!!.toLowerCase(Locale.getDefault())
                            .contains(p0.toString())
                    ) {
                        tempArr.add(arr)
                    }
                }

                categoryAdapter.setData(tempArr)
                categoryAdapter.notifyDataSetChanged()
                return true
            }
        })
    }
}