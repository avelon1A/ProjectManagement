package com.example.projectmanagement.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.adapter.BoardAdapter
import com.example.projectmanagement.adapter.BoardClicklistner
import com.example.projectmanagement.adapter.TaskListItemsAdapter
import com.example.projectmanagement.databinding.FragmentBoardBinding
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.model.Task
import com.example.projectmanagement.viewModel.TaskFragmentViewModel
import kotlinx.coroutines.launch


class BoardFragment : Fragment(), BoardClicklistner {
private  var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    val args: BoardFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardAdapter: TaskListItemsAdapter
    private lateinit var viewModel: TaskFragmentViewModel
    private lateinit var dataList:ArrayList<Task>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = args.boardId
        viewModel = TaskFragmentViewModel()
//        viewModel.saveTaskInfo(boardId,Task("task1","aman"))
//        viewModel.getBoardById(boardId)
         dataList = ArrayList()
        dataList.add(Task(resources.getString(R.string.add_list)))


        recyclerView = binding.rvTaskList


        lifecycleScope.launch(){
            viewModel.response.collect{
                when(it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Log.d("rv_task","${it.data}")
                        boardAdapter = it.data?.let { it1 -> TaskListItemsAdapter(requireContext(), it1.task,this@BoardFragment) }!!
                        recyclerView.adapter = boardAdapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    }
                    else -> {

                    }
                }
            }
        }



    }

    override fun onItemClick( model: Task) {


    }

    override fun doneClick(name: String) {
        dataList.add(Task(name))
    }



}