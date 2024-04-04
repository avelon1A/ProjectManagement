package com.example.projectmanagement.ui.fragment

import RegisterValidation
import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentSigninBinding
import com.example.projectmanagement.viewModel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignIn : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModel:LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = LoginViewModel()
        binding.btnSignIn.setOnClickListener {
            val email =binding.etEmail.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            viewModel.login(email,password)
        }
        lifecycleScope.launch(){
            viewModel.result.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.btnSignIn.visibility = View.GONE
                        binding.buttonAnimation.visibility = View.VISIBLE
                        binding.buttonAnimation.playAnimation()
                    }
                    is Resource.Success -> {

                        findNavController().navigate(R.id.action_signIn_to_homeActivity)
                    }
                    else -> {
                        binding.btnSignIn.visibility = View.VISIBLE
                        binding.buttonAnimation.visibility = View.GONE
                        toast("Login Failed")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.validation.collect(){
                if(it.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if(it.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etPasswordLogin.apply {
                            requestFocus()
                            error = it.password.message
                        } } } }
        }

    }
    private fun toast(result: String?) {
        Toast.makeText(requireContext(), result, Toast.LENGTH_LONG)?.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}