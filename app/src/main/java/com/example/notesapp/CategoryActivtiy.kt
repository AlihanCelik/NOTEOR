package com.example.notesapp

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.CategoryActivtiyAdapter
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.activity_category_activtiy.*
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