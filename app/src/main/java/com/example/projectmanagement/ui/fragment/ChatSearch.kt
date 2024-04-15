package com.example.projectmanagement.ui.fragment

import Resource
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
import com.example.projectmanagement.databinding.FragmentChatSearchBinding
import com.example.projectmanagement.uitl.LoadingDialogUtil
import com.example.projectmanagement.viewModel.ChatSearchViewModel
import kotlinx.coroutines.launch

class ChatSearch : Fragment(), ChatSearchClick {
    private var _binding: FragmentChatSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatSearchAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatSearchViewModel
    private lateinit var onBackPressedCallback: OnBackPressedCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.action_chatSearch_to_homeFragment)
//            }
//        }

//        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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

        viewModel = ChatSearchViewModel()
        recyclerView = binding.chatSearch

            binding.tvTitleTask.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.search(binding.tvTitleTask.text.toString())
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
                    }

                    else -> {
                        LoadingDialogUtil.dismissLoadingDialog()
                    }
                }
            }
        }
    }

    override fun onItemClick(uid: String) {
        Log.d("uid","$uid")
        val action = ChatSearchDirections.actionChatSearchToChatFragment(uid)
        findNavController().navigate(action)

    }
}