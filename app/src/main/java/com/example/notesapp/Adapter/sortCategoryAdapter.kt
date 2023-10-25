package com.example.notesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.item_category_sort.view.*

class sortCategoryAdapter() :
    RecyclerView.Adapter<sortCategoryAdapter.SortCategoryViewHolder>() {
    var arrList = ArrayList<Category>()
    class SortCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortCategoryViewHolder {
        return SortCategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_sort,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
    fun updateData(newCategoryList: List<Category>) {
        arrList.clear()
        arrList.addAll(newCategoryList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SortCategoryViewHolder, position: Int) {
        val model =arrList[position]
        holder.itemView.item_sortCategory_text.text=model.name_category



    }
}