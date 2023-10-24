package com.example.notesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.item_bottom_sheet_category.view.*

class CategoryAdapter (private val clickListener: CategoryClickListener,var adapterPosition: Int=-1) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var arrList = ArrayList<Category>()
    class CategoryViewHolder(view:View) : RecyclerView.ViewHolder(view){

    }
    interface CategoryClickListener {
        fun onCategoryClick(category: Category)
    }
    fun setData(arrCategoryList: List<Category>){
        arrList = ArrayList(arrCategoryList)
    }
    fun updateData(newCategoryList: List<Category>) {
        arrList.clear()
        arrList.addAll(newCategoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bottom_sheet_category, parent, false))
    }



    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model =arrList[position]
        holder.itemView.item_text_categories.text=model.name_category
        holder.itemView.setOnClickListener {
            adapterPosition = arrList[position].id_category!!
            notifyDataSetChanged()
            clickListener.onCategoryClick(model)
        }
        if (arrList[position].id_category == adapterPosition){
            holder.itemView.findViewById<CardView>(R.id.item_back_category).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue1))
            holder.itemView.item_text_categories.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }else {
            holder.itemView.findViewById<CardView>(R.id.item_back_category).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.grey3))
            holder.itemView.item_text_categories.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey2))
        }
    }
    override fun getItemCount(): Int {
        return arrList.size
    }
}