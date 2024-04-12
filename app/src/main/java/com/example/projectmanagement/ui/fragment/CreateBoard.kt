package com.example.projectmanagement.ui.fragment

import Resource
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentCreateBoardBinding
import com.example.projectmanagement.ui.activity.CustomLoadingDialog
import com.example.projectmanagement.uitl.LoadingDialogUtil
import com.example.projectmanagement.uitl.LoadingDialogUtil.dismissLoadingDialog
import com.example.projectmanagement.uitl.LocalData
import com.example.projectmanagement.viewModel.CreateBoardViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch


class CreateBoard : Fragment() {
    private var _binding: FragmentCreateBoardBinding? = null
    private lateinit var currentUser: String
    private lateinit var localData: LocalData
    private var selectedImageUri: Uri? = null
    private lateinit var viewModel: CreateBoardViewModel
    private lateinit var storage: FirebaseStorage


    private val image = ""
    private val binding get() = _binding!!

    private val imagePickContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                if (selectedImageUri != null) {
                    binding.imageBoard.setImageURI(selectedImageUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localData = LocalData(requireContext())
        currentUser = localData.getCurrentuser().toString()
        viewModel = CreateBoardViewModel()
        storage = FirebaseStorage.getInstance()


        binding.imageBoard.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            imagePickContract.launch(intent)
        }
        binding.createBoard.setOnClickListener {
            val name = binding.etNameBoard.text.toString()
            LoadingDialogUtil.showLoadingDialog(requireContext())
            if (name.isEmpty()) {
                binding.etNameBoard.apply {
                    requestFocus()
                    error = "name cannot be empty"
                }
            }
            viewModel.createBoard(name, currentUser, selectedImageUri!!)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launch() {
            viewModel.response.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        dismissLoadingDialog()
                        findNavController().navigate(R.id.action_createBoard_to_homeFragment)
                    }
                    else -> {
                        dismissLoadingDialog()
                    }
                }
            }
        }
    }
}