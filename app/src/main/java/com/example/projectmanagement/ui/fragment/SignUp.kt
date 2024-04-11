package com.example.projectmanagement.ui.fragment

import RegisterValidation
import Resource
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projectmanagement.viewModel.SignUpViewModel
import com.example.projectmanagement.databinding.FragmentSignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = SignUpViewModel()
        binding.btnSignUp.setOnClickListener {
            val username = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.createUser(username, email, password)
        }
        lifecycleScope.launch {
            viewModel.response.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                        binding.btnSignUp.visibility = View.GONE
                        binding.buttonAnimation.visibility = View.VISIBLE
                        binding.buttonAnimation.playAnimation()

                    }

                    is Resource.Success -> {
                        binding.btnSignUp.visibility = View.VISIBLE
                        binding.buttonAnimation.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        Log.d("test", resource.message.toString())
                        binding.btnSignUp.visibility = View.VISIBLE
                        binding.buttonAnimation.visibility = View.GONE
                    }

                    else -> {

                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.validation.collect() {
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if (it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}