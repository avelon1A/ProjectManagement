package com.example.projectmanagement.ui.fragment

import CustomColorPickerDialog
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
import com.example.projectmanagement.databinding.FragmentCardDetailBinding
import com.example.projectmanagement.model.Card
import com.example.projectmanagement.uitl.LoadingDialogUtil.dismissLoadingDialog
import com.example.projectmanagement.uitl.LoadingDialogUtil.showLoadingDialog
import com.example.projectmanagement.viewModel.CardDetailViewModel
import kotlinx.coroutines.launch


class CardDetailFragment : Fragment() {

    private var _binding: FragmentCardDetailBinding? = null
    private val binding get() = _binding!!
    var card: Card? = null
    val args: CardDetailFragmentArgs by navArgs()
    private var taskPositon: Int? = null
    private var cardPositon: Int? = null
    var boardId: String? = null
    private lateinit var viewModel: CardDetailViewModel
    private var lableColor: String? = null

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
        binding.etNameCardDetails.setText(card!!.name)

        if (card!!.labelColor.isNotEmpty()){
            binding.tvSelectLabelColor.text = null
            binding.tvSelectLabelColor.setBackgroundColor(card!!.labelColor.toInt())
        }

        binding.ivDeleteCard.setOnClickListener {
            viewModel.deleteCard(boardId!!, taskPositon!!, cardPositon!!)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvSelectLabelColor.setOnClickListener {
            CustomColorPickerDialog(requireContext()) { selectedColor ->
                binding.tvSelectLabelColor.text = null
                binding.tvSelectLabelColor.setBackgroundColor(selectedColor)
                lableColor = selectedColor.toString()
                Log.d("lalbleColor","$lableColor")

            }
        }
        binding.btnUpdateCardDetails.setOnClickListener {
            viewModel.updateCard(boardId!!, taskPositon!!, cardPositon!!,lableColor!!)
        }

        lifecycleScope.launch {
            viewModel.deleteCard.collect {
                when (it) {
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