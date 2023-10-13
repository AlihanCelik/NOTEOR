package com.example.notesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.item_bottom_sheet_category.view.*

class CategoryAdapter () :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var arrList = ArrayList<Category>()


    class CategoryViewHolder(view:View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bottom_sheet_category, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model =arrList[position]
        holder.itemView.item_text_categories.text=model.name_category
    }
    override fun getItemCount(): Int {
        return arrList.size
    }
}