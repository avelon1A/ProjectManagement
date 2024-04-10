package com.example.projectmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.model.Board

class BoardAdapter(private val context: Context, private val dataList: ArrayList<Board>,
                 private val itemClickListener: BoardItemClickListener
                   ) :
    RecyclerView.Adapter<BoardAdapter.YourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return YourViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item.id,item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class YourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.rv_title)
        private val image:ImageView = itemView.findViewById(R.id.rv_pic)
        fun bind(item: Board) {
            textViewName.text = item.name
            Glide.with(context).load(item.image).into(image)
        }
    }
}

interface BoardItemClickListener {
    fun onItemClick(boardId: String ,model:Board)
}

