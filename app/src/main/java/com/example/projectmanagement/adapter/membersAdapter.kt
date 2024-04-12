package com.example.projectmanagement.adapter

import User
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import de.hdodenhof.circleimageview.CircleImageView

class MembersAdapter(
    private val context: Context, private val dataList: ArrayList<User>,

    ) :
    RecyclerView.Adapter<MembersAdapter.YourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_members, parent, false)
        return YourViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourViewHolder, position: Int) {
        val model = dataList[position]
        if (holder is YourViewHolder) {
            holder.name.text = model.username
            holder.email.text = model.email
            Glide.with(context).load(model.imageURL).into(holder.image)

        }

    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class YourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_member_name)
        val email: TextView = itemView.findViewById(R.id.tv_member_email)
        val image: CircleImageView = itemView.findViewById(R.id.iv_member_image)
    }
}



