package com.example.projectmanagement.ui.fragment

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectmanagement.databinding.ColorPickerBinding
import com.example.projectmanagement.databinding.FragmentCardDetailBinding
import com.example.projectmanagement.model.Card
import com.example.projectmanagement.ui.activity.CustomColorPickerDialog
import com.example.projectmanagement.uitl.LoadingDialogUtil.dismissLoadingDialog
import com.example.projectmanagement.uitl.LoadingDialogUtil.showLoadingDialog
import com.example.projectmanagement.viewModel.CardDetailViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CardDetailFragment : Fragment() {

    private var _binding: FragmentCardDetailBinding? = null
    private val binding get() = _binding!!
    var card: Card? = null
    val args: CardDetailFragmentArgs by navArgs()
    private var taskPositon:Int?= null
    private var cardPositon:Int?= null
    var boardId:String? =  null
    private lateinit var viewModel:CardDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card = args.card
        taskPositon = args.taskPosition
        cardPositon = args.cardPosition
        boardId = args.boardId
        viewModel = CardDetailViewModel()


        binding.tvCardName.text = card!!.name
        binding.ivDeleteCard.setOnClickListener {
          viewModel.deleteCard(boardId!!, taskPositon!!, cardPositon!!)
        }
        binding.tvSelectLabelColor.setOnClickListener {
            CustomColorPickerDialog(requireContext()) { selectedColor ->
                binding.tvSelectLabelColor.setBackgroundColor(selectedColor)
            }
        }

        lifecycleScope.launch {
            viewModel.deleteCard.collect {
                when(it){
                    is Resource.Loading -> {
                        showLoadingDialog(requireContext())
                    }
                    is Resource.Success -> {
                        findNavController().navigateUp()
                        dismissLoadingDialog()
                    }

                    else -> {
                        dismissLoadingDialog()
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