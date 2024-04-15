package com.example.projectmanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
class ChatViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val senderUid: String? = auth.currentUser?.uid
    private var receiverUid: String? = null

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    fun setReceiverUid(receiverUid: String) {
        this.receiverUid = receiverUid
        loadMessages()
    }

    fun sendMessage(message: String) {
        if (senderUid != null && receiverUid != null) {
            val timestamp = System.currentTimeMillis()
            val messageObject = Message(senderUid, receiverUid, message, timestamp)
            database.child("chats").child(senderUid).child(receiverUid!!).push().setValue(messageObject)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                }
            // For two-way communication, also save the message in the receiver's node
            database.child("chats").child(receiverUid!!).child(senderUid).push().setValue(messageObject)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                }
        }
    }

    private fun loadMessages() {
        if (senderUid != null && receiverUid != null) {
            database.child("chats").child(senderUid).child(receiverUid!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messageList = mutableListOf<Message>()
                        for (messageSnapshot in snapshot.children) {
                            val messageMap = messageSnapshot.value as Map<String, Any>
                            val message = Message(
                                senderUid = messageMap["senderUid"] as String?,
                                receiverUid = messageMap["receiverUid"] as String?,
                                message = messageMap["message"] as String?,
                                timestamp = messageMap["timestamp"] as Long?
                            )
                            messageList.add(message)
                        }
                        _messages.value = messageList
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}
