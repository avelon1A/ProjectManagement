package com.example.projectmanagement.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private lateinit var messageAdapter: MessageAdapter
    private    val args: ChatSearchArgs by navArgs()

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
        val reciverid = args.reciverUid
        val currentUser = localData.getCurrentUserId()

        messageAdapter = MessageAdapter(emptyList(),currentUser!!)
        binding.chatRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.messages = messages
            messageAdapter.notifyDataSetChanged()
        }
        viewModel.setReceiverUid(reciverid)

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageEditText.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
