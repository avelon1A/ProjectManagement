package com.example.projectmanagement.ui.fragment

import Resource
import User
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.adapter.ChatSearchAdapter
import com.example.projectmanagement.adapter.ChatSearchClick
import com.example.projectmanagement.adapter.SearchSavedAdapter
import com.example.projectmanagement.databinding.FragmentChatSearchBinding
import com.example.projectmanagement.uitl.LoadingDialogUtil
import com.example.projectmanagement.uitl.LocalData
import com.example.projectmanagement.viewModel.ChatSearchViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class ChatSearch : Fragment(), ChatSearchClick {
    private var _binding: FragmentChatSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatSearchAdapter
    private lateinit var adapter2: SearchSavedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatSearchViewModel
    private lateinit var localData: LocalData
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var previousSearch:ArrayList<User>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatSearchBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
       localData = LocalData(requireContext())
        viewModel = ChatSearchViewModel()
        recyclerView = binding.chatSearch
        val previousSearch = localData.getSearch()
        if( !previousSearch.isNullOrEmpty()){
            adapter2 = previousSearch?.let {
                SearchSavedAdapter(requireContext(), it,this@ChatSearch) }!!
            recyclerView.adapter = adapter2
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

            binding.tvTitleTask.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchUser = binding.tvTitleTask.text.toString()
                    viewModel.search(searchUser)
                    return@setOnEditorActionListener true

                }
                false
            }

            lifecycleScope.launch {
            viewModel.chatResult.collect {
                when (it) {
                    is Resource.Loading -> {
                        LoadingDialogUtil.showLoadingDialog(requireContext())
                    }

                    is Resource.Success -> {
                        adapter = it.data?.let { it1 -> ChatSearchAdapter(requireContext(), it1,this@ChatSearch) }!!
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        LoadingDialogUtil.dismissLoadingDialog()
                        localData.saveSearch(it.data)

                    }

                    else -> {
                        LoadingDialogUtil.dismissLoadingDialog()
                    }
                }
            }
        }
    }


    override fun onItemClick(uid: String, name: String) {
        Log.d("uid","$uid")
        val action = ChatSearchDirections.actionChatSearchToChatFragment(uid,name)
        findNavController().navigate(action)    }
}