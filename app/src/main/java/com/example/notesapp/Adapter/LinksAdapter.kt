package com.example.notesapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import kotlinx.android.synthetic.main.item_links.view.*

class LinksAdapter constructor(
    private var context: Context,
    private var items_Links: MutableList<String>,
    private var layoutLinkPreview: View
) : RecyclerView.Adapter<LinksAdapter.LinkViewHolder>() {


    inner class LinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.item_btn_delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItemAtPosition(position)
                }
            }
        }
    }
    fun removeItemAtPosition(position: Int) {
        if (position in 0 until items_Links.size) {
            items_Links.removeAt(position)
            notifyItemRemoved(position)
            if (items_Links.isEmpty()) {
                layoutLinkPreview.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        return LinkViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_links, parent, false))
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val model = items_Links[position]
        holder.itemView.item_text_Link.text = model
        holder.itemView.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(holder.itemView.item_text_Link.text.toString()))
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return items_Links.size
    }
}