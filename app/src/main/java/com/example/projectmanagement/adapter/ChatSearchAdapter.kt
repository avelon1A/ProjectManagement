package com.example.projectmanagement.adapter

import User
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.model.Board
import de.hdodenhof.circleimageview.CircleImageView

class ChatSearchAdapter(
    private val context: Context, private val List: ArrayList<User>,
    private val itemClickListener: ChatSearchClick
    ) : RecyclerView.Adapter<ChatSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = List[position]
        if (holder is ViewHolder) {
            holder.textViewName.text = item.username
            Glide.with(context).load(item.imageURL).into(holder.image)

            holder.itemView.setOnClickListener {
                itemClickListener.onItemClick(item.uid)
            }
        }

    }

    override fun getItemCount(): Int {
        return List.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.search_name)
        val image: CircleImageView = itemView.findViewById(R.id.search_image)

    }


}

interface ChatSearchClick {
    fun onItemClick(uid:String)
}