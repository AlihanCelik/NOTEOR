package com.example.notesapp.adThen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import kotlinx.android.synthetic.main.item_images.view.*

class ImageAdapter constructor(
    private var context: Context,
    private var items: MutableList<Uri>,
    private var layoutImgPreview: View
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.item_image_img)
        init {
            itemView.btn_clear_added_img.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItemAtPosition(position)
                }
            }
        }
    }
    fun removeItemAtPosition(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            if (items.isEmpty()) {
                layoutImgPreview.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_images, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        Glide.with(context).load(model).into(holder.image)
        holder.itemView.setOnClickListener{
            val intent= Intent(context, PicturesActivty::class.java)
            intent.putExtra("image",model.toString())
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return items.size
    }
}