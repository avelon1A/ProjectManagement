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
import com.bumptech.glide.Glide
import com.example.projectmanagement.databinding.FragmentSearchBinding
import com.example.projectmanagement.ui.activity.CustomLoadingDialog
import com.example.projectmanagement.viewModel.SearchViewModel
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private var addMember: String = "default"
    val args: SearchFragmentArgs by navArgs()
    private var boardId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = SearchViewModel()
        boardId = args.boardId
        val loading = CustomLoadingDialog(requireContext())



        binding.buttonSearch.setOnClickListener {
            val searchName = binding.editTextSearch.text.toString()
            Log.d("searchName", searchName)
            viewModel.search(searchName)
        }


        lifecycleScope.launch() {
            viewModel.memberList.collect {
                when (it) {
                    is Resource.Loading -> {
                        loading.show()
                    }
                    is Resource.Success -> {
                        binding.cvSearch.visibility = View.VISIBLE
                        val imageUrl = binding.userImage
                        Glide.with(requireActivity()).load(it.data?.imageURL).into(imageUrl)
                        binding.username.text = it.data!!.username
                        binding.email.text = it.data!!.email
                        addMember = it.data.uid
                        loading.dismiss()
                    }
                    else -> {
                        loading.dismiss()
                    }
                }
            }
        }

        Log.d("addMember", "$addMember")
        binding.buttonAdd.setOnClickListener {
            viewModel.addMember(addMember, boardId)

        }
        lifecycleScope.launch() {
            viewModel.add.collect {
                when (it) {
                    is Resource.Loading -> {
                        loading.show()
                    }
                    is Resource.Success -> {
                        findNavController().navigateUp()
                        loading.dismiss()
                    }
                    else -> {
                        loading.dismiss()
                    }
                }
            }
        }


    }

}