package com.example.notesapp.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.database.CategoryDatabase
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Category
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_favorites_activtity.*
import kotlinx.android.synthetic.main.delete_permi_dialog.view.*
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
        GlobalScope.launch(Dispatchers.Main){
            let {
                var notes = NotesDatabase.getDatabase(context).noteDao().getAllNotes()
                var arrNotes = notes as ArrayList<Notes>
                var idArr = ArrayList<Notes>()
                for (arr in arrNotes){
                    if(arr.noteCategoryId==model.id_category){
                        idArr.add(arr)
                    }
                }
                if(model.name_category!="All Notes"){
                    holder.itemView.item_category_activtiy_size.visibility=View.VISIBLE
                    holder.itemView.item_category_activtiy_size.text="(${idArr.size})"
                }else{
                    holder.itemView.item_category_activtiy_size.visibility=View.GONE
                }

            }

        }
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
            view.category_name.hint=model.name_category.toString()
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

        holder.itemView.category_delete.setOnClickListener {
            val view3 = View.inflate(context, R.layout.delete_permi_dialog, null)
            val builder3 = AlertDialog.Builder(context)
            builder3.setView(view3)
            val dialog3 = builder3.create()
            dialog3.show()
            dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
            view3.cancel_delete_permi.setOnClickListener{
                dialog3.dismiss()
            }
            view3.yes_delete_permi.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    model.id_category?.let { it1 ->
                        CategoryDatabase.getDatabase(context).CategoryDao().deleteSpecificCategory(
                            it1
                        )
                    }
                }
                arrList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT).show()
                dialog3.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}