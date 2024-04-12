package com.example.projectmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.model.Card

class CardAdapter(
    private val context: Context,
    private val list: ArrayList<Card>,
    private val itemClick: CarditemClick,
    private val taskPosition: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            holder.itemview.text = model.name
            holder.card.setOnClickListener {
                itemClick.onItemClick(model, position,taskPosition)

            }


        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemview: TextView = itemView.findViewById(R.id.tv_card_name)
        val card: LinearLayout = itemView.findViewById(R.id.card)

    }
}

interface CarditemClick {
    fun onItemClick(model: Card, cardPosition: Int, taskPosition: Int)
}

