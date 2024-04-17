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
import de.hdodenhof.circleimageview.CircleImageView

class SearchSavedAdapter(
    private val context: Context, private val List: ArrayList<User>,
    private val itemClickListener: ChatSearchClick
) : RecyclerView.Adapter<SearchSavedAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSavedAdapter.myViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val item = List[position]
        if (holder is ViewHolder) {
            holder.textViewName.text = item.username
            Glide.with(context).load(item.imageURL).into(holder.image)

            holder.itemView.setOnClickListener {
                itemClickListener.onItemClick(item.uid,item.username)
            }
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }


    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.search_name)
        val image: CircleImageView = itemView.findViewById(R.id.search_image)

    }


}
