package com.example.projectmanagement.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.adapter.BoardClicklistner
import com.example.projectmanagement.adapter.CarditemClick
import com.example.projectmanagement.adapter.TaskListItemsAdapter
import com.example.projectmanagement.databinding.FragmentBoardBinding
import com.example.projectmanagement.model.Card
import com.example.projectmanagement.model.Task
import com.example.projectmanagement.uitl.LocalData
import com.example.projectmanagement.viewModel.TaskFragmentViewModel
import kotlinx.coroutines.launch


class BoardFragment : Fragment(), BoardClicklistner,CarditemClick {
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    val args: BoardFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardAdapter: TaskListItemsAdapter
    private lateinit var viewModel: TaskFragmentViewModel
    private lateinit var boardId: String
    private lateinit var BoardName: String
    private lateinit var localData: LocalData
    private lateinit var currentUser: String
    private lateinit var currentId: String
    private var isTaskAdded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardId = args.boardId
        BoardName = args.BoardName
        viewModel = TaskFragmentViewModel()
        viewModel.getBoardByIdAndListen(boardId)

        localData = LocalData(requireContext())
        currentUser = localData.getCurrentuser().toString()
        currentId = localData.getCurrentUserId().toString()
        Log.d("currentUserNametest", "$currentId")
        Log.d("currentUserNametest", "$currentUser")


        binding.ivOverflowMenu.setOnClickListener {
            showOverflowMenu()
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        recyclerView = binding.rvTaskList
        binding.tvTitleTask.text = BoardName


        lifecycleScope.launch() {
            viewModel.response.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Log.d("rv_task", "${it.data}")
                        boardAdapter = it.data?.let { it1 ->
                            TaskListItemsAdapter(requireContext(), it1.task, this@BoardFragment,this@BoardFragment) }!!
                        recyclerView.adapter = boardAdapter
                        recyclerView.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        localData.saveAssignedTo(it.data.assignedTo)
                    }

                    else -> {

                    }
                }
            }
        }
     val assignTo =    localData.getAssignedTO()


    }

    private fun showOverflowMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.ivOverflowMenu)
        popupMenu.menuInflater.inflate(R.menu.members_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.member_menu -> {
              val action = BoardFragmentDirections.actionBoardFragmentToMembersFragment(boardId,BoardName)
                    findNavController().navigate(action)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onItemClick(model: Task) {


    }

    override fun addTask(name: String) {
        viewModel.saveTaskInfo(boardId, Task(name, currentId))
        recyclerView.scrollToPosition(0)
    }

    override fun updateTaskName(index: Int, name: String) {
        viewModel.updateTaskName(boardId, index, name)
    }

    override fun deleteTask(position: Int) {
        viewModel.deleteTask(boardId, position)
    }

    override fun createCard(name: String, position: Int) {
        var card = Card(name = name, createdBy = currentId, assignedTo = ArrayList())
        card.assignedTo.add(currentId)
        viewModel.addCardToTask(boardId, position, card)
    }



    override fun onItemClick(model: Card, cardPosition: Int, taskPosition: Int) {
        val action = BoardFragmentDirections.actionBoardFragmentToCardDetailFragment(model,cardPosition,taskPosition,boardId)
        findNavController().navigate(action)
    }


}