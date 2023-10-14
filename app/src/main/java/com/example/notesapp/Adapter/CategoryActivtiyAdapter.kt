package com.example.notesapp.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Category

class CategoryActivtiyAdapter() :
    RecyclerView.Adapter<CategoryActivtiyAdapter.CategoryActViewHolder>() {
    var arrList = ArrayList<Category>()
    class CategoryActViewHolder(view:View) : RecyclerView.ViewHolder(view){

    }

    fun setData(arrCategoryList: List<Category>){
        arrList = ArrayList(arrCategoryList)
    }
    fun updateData(newCategoryList: List<Category>) {
        arrList.clear()
        arrList.addAll(newCategoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryActViewHolder {
        return CategoryActViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_activtiy, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryActViewHolder, position: Int) {
        val model =arrList[position]
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}