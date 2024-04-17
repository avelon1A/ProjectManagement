package com.example.projectmanagement.adapter


import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.model.Task

// START
open class TaskListItemsAdapter(
    private val context: Context,
    private var list: List<Task>,
    private val itemClickListener: BoardClicklistner,
    private val carditemClick: CarditemClick
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


        if (position < list.size ) {
            val model = list[position]
            if (holder is MyViewHolder) {

                    holder.tvAddCardList.visibility = View.GONE
                    holder.llTaskItem.visibility = View.VISIBLE

                holder.tvAddCardList.setOnClickListener {
                    holder.tvAddCardList.visibility = View.GONE
                    holder.CvList.visibility = View.VISIBLE
                    itemClickListener.onItemClick(model)
                }
                if (list.isNotEmpty()) {
                    holder.llTaskList.visibility = View.VISIBLE
                    holder.tvTaskListTitle.text = list[position].title

                }


                holder.ibEditListName.setOnClickListener {
                    holder.llTaskList.visibility = View.GONE
                    holder.cvEditTaskListName.visibility = View.VISIBLE
                    holder.addCard.visibility = View.GONE
                }
                holder.ibDoneEditListName.setOnClickListener {
                    if (holder.editTaskListName.text.isNotEmpty()) {
                        itemClickListener.updateTaskName(
                            position,
                            holder.editTaskListName.text.toString()
                        )

                    } else {
                        holder.editTaskListName.apply {
                            requestFocus()
                            error = "name cannot be empty"
                        }
                    }
                }
                holder.ibCloseEdit.setOnClickListener {
                    holder.llTaskList.visibility = View.VISIBLE
                    holder.cvEditTaskListName.visibility = View.GONE
                }
                holder.ibDeleteTask.setOnClickListener {
                    itemClickListener.deleteTask(position)
                }
                holder.addCard.setOnClickListener {
                    holder.addCardName.visibility = View.VISIBLE
                    holder.addCard.visibility = View.GONE
                }
                holder.ibCardNameDone.setOnClickListener {
                    if (holder.etAddCardName.text.toString().isNotEmpty()) {
                        itemClickListener.createCard(holder.etAddCardName.text.toString(), position)
                    } else
                        holder.etAddCardName.apply {
                            requestFocus()
                            error = "card name cannot be empty"
                        }
                }
                holder.rvCardList.layoutManager = LinearLayoutManager(context)
                holder.rvCardList.setHasFixedSize(true)
                val adapter = CardAdapter(context,model.cards,carditemClick,position)
                holder.rvCardList.adapter = adapter



            }
        } else {
            if (holder is MyViewHolder) {
                holder.tvAddCardList.visibility = View.VISIBLE
                holder.llTaskItem.visibility = View.GONE
                holder.tvAddCardList.setOnClickListener {
                    holder.tvAddCardList.visibility = View.GONE
                    holder.CvList.visibility =View.VISIBLE

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
    }

    override fun getItemCount(): Int {
        return list.size +1
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
        val cvEditTaskListName: CardView = view.findViewById(R.id.cv_edit_task_list_name)
        val ibEditListName: ImageButton = view.findViewById(R.id.ib_edit_list_name)
        val ibDoneEditListName: ImageButton = view.findViewById(R.id.ib_done_edit_list_name)
        val editTaskListName: EditText = view.findViewById(R.id.et_edit_task_list_name)
        val ibCloseEdit: ImageButton = view.findViewById(R.id.ib_close_editable_view)
        val ibDeleteTask: ImageButton = view.findViewById(R.id.ib_delete_list)
        val addCard: TextView = view.findViewById(R.id.tv_add_card)
        val addCardName: CardView = view.findViewById(R.id.cv_add_card)
        val ibCardNameDone: ImageButton = view.findViewById(R.id.ib_done_card_name)
        val etAddCardName: EditText = view.findViewById(R.id.et_card_name)
        val rvCardList:RecyclerView = view.findViewById(R.id.rv_card_list)

    }
}

interface BoardClicklistner {
    fun onItemClick(model: Task)
    fun addTask(name: String)
    fun updateTaskName(index:Int,name:String)
     fun deleteTask(position: Int)
     fun createCard(name: String,index:Int)
}
