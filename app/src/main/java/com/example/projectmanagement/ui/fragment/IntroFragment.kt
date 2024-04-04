package com.example.projectmanagement.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {
    private var _binding:FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentIntroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUpIntro.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signUp)
        }
        binding.btnSignInIntro.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signIn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}