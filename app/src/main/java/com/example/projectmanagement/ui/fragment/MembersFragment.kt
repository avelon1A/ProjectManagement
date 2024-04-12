package com.example.projectmanagement.ui.fragment

import Resource
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.adapter.MembersAdapter
import com.example.projectmanagement.databinding.FragmentMembersBinding
import com.example.projectmanagement.uitl.LocalData
import com.example.projectmanagement.viewModel.MembersListViewModel
import kotlinx.coroutines.launch


class MembersFragment : Fragment() {
    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MembersAdapter
    private lateinit var boardId: String
    private lateinit var boardName:String
    private lateinit var viewModel: MembersListViewModel
    private lateinit var localData: LocalData
    val args: MembersFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = MembersListViewModel()
        localData = LocalData(requireContext())
        boardId = args.boardId
        boardName = args.BoardName
        val assignedTo = localData.getAssignedTO()
        Log.d("assignedto", "$assignedTo")
        if (assignedTo != null) {
            viewModel.getMembersList(assignedTo)
        }
        binding.ivMemberAdd.setOnClickListener {
             val action = MembersFragmentDirections.actionMembersFragmentToSearchFragment(boardId,boardName)
            findNavController().navigate(action)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        recyclerView = binding.rvMembersList

        lifecycleScope.launch() {
            viewModel.memberList.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Log.d("rv_task", "${it.data}")
                        adapter = MembersAdapter(requireContext(), it.data!!)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }

                    else -> {
                    }
                }
            }
        }

    }

}