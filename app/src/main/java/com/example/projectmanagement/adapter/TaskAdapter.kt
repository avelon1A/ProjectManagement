package com.example.projectmanagement.adapter


import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.model.Task

// START
open class TaskListItemsAdapter(
    private val context: Context,
    private var list: List<Task>,
    private val itemClickListener: BoardClicklistner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_task_list, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        view.layoutParams = layoutParams

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        Log.d("list","$list")

        if (holder is MyViewHolder) {

            if (position == list.size - 1) {
                holder.tvAddCardList.visibility = View.VISIBLE
                holder.llTaskItem.visibility = View.GONE
            } else {
                holder.tvAddCardList.visibility = View.GONE
                holder.llTaskItem.visibility = View.VISIBLE
            }
            holder.tvAddCardList.setOnClickListener {
                holder.tvAddCardList.visibility = View.GONE

            }
            holder.tvAddCardList.setOnClickListener {
                holder.tvAddCardList.visibility = View.GONE
                holder.CvList.visibility = View.VISIBLE
                itemClickListener.onItemClick(model)
            }
            if (list.isNotEmpty()){
              holder.llTaskList.visibility = View.VISIBLE
                holder.tvTaskListTitle.text = list[position].title

            }

            if (!holder.editName.text.isNotEmpty()) {
                holder.doneButton.setOnClickListener {
                    val name = holder.editName.text.toString()
                    itemClickListener.addTask(name)
                    holder.CvList.visibility = View.GONE


                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAddCardList: TextView = view.findViewById(R.id.tv_add_task_list)
        val llTaskItem: LinearLayout = view.findViewById(R.id.ll_task_item)
        val CvList: CardView = view.findViewById(R.id.cv_add_task_list_name)
        val editName: EditText = view.findViewById(R.id.et_task_list_name)
        val doneButton: ImageButton = view.findViewById(R.id.ib_done_list_name)
        val llTaskList: LinearLayout = view.findViewById(R.id.ll_title_view)
        val tvTaskListTitle: TextView = view.findViewById(R.id.tv_task_list_title)




    }
}

interface BoardClicklistner {
    fun onItemClick(model: Task)
    fun addTask(name: String)
}
