package com.example.notesapp.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Category
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.item_category_activtiy.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val context = holder.itemView.context
        val model = arrList[position]
        holder.itemView.item_category_activty_name.text = model.name_category
        if (arrList[position].name_category == "All Notes") {
            holder.itemView.category_rename.visibility = View.GONE
            holder.itemView.category_delete.visibility = View.GONE
        } else {
            holder.itemView.category_rename.visibility = View.VISIBLE
            holder.itemView.category_delete.visibility = View.VISIBLE
        }
        holder.itemView.category_rename.setOnClickListener {
            val view = View.inflate(context, R.layout.dialog_add_category, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            view.okeyCategory.setOnClickListener {
                val newCategoryName = view.category_name.text.toString()
                model.name_category=newCategoryName
                GlobalScope.launch(Dispatchers.IO) {
                   CategoryDatabase.getDatabase(context).CategoryDao().updateCategory(arrList[position])
                }
                notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}