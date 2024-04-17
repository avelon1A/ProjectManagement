package com.example.projectmanagement.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.adapter.MessageAdapter
import com.example.projectmanagement.databinding.FragmentChatBinding
import com.example.projectmanagement.uitl.LocalData
import com.example.projectmanagement.viewModel.ChatViewModel

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var localData: LocalData
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatSearchArgs by navArgs()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localData = LocalData(requireContext())
        val receiverUid = args.reciverUid
        val reciverName = args.recivername
        val currentUser = localData.getCurrentUserId()
        setupRecyclerView(currentUser!!)
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.messages = messages
            messageAdapter.notifyDataSetChanged()
            binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
        }
        viewModel.setReceiverUid(receiverUid)
        setupClickListeners()
        binding.tvTitleTask.text = reciverName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(currentUser: String) {
        messageAdapter = MessageAdapter(emptyList(), currentUser)
        binding.chatRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageEditText.setText("")
            }
            binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)

        }
    }
}
