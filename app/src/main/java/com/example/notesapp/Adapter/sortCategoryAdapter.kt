package com.example.notesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.item_category_sort.view.*

class sortCategoryAdapter(private var listener: SortCategoryClickListener) :
    RecyclerView.Adapter<sortCategoryAdapter.SortCategoryViewHolder>() {
    var arrList = ArrayList<Category>()
    var selectedCategory=1;
    class SortCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortCategoryViewHolder {
        return SortCategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_sort,parent, false)
        )
    }
    interface SortCategoryClickListener {
        fun onSortCategoryClick(category: Category)
    }
    fun setData(arrCategoryList: List<Category>){
        arrList = ArrayList(arrCategoryList)
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

        holder.itemView.setOnClickListener {
            selectedCategory = model.id_category!!
            notifyDataSetChanged()
            listener.onSortCategoryClick(model)

        }
        if (arrList[position].id_category==selectedCategory) {
            holder.itemView.item_sort_category.visibility = View.VISIBLE
        } else {
            holder.itemView.item_sort_category.visibility = View.GONE
        }



    }
}