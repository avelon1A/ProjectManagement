package com.example.projectmanagement.viewModel

import Resource
import User
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel : ViewModel() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _memberList = MutableStateFlow<Resource<User>>(Resource.unSpecified())
    val memberList: Flow<Resource<User>> = _memberList

    val _added = MutableStateFlow<Resource<String>>(Resource.unSpecified())
    val add: Flow<Resource<String>> = _added

    fun search(email: String) {
        _memberList.value = Resource.Loading()

        db.collection(Constant.USER_COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userList = User()
                val document = querySnapshot.documents[0]
                val user = document.toObject(User::class.java)
                _memberList.value = Resource.Success(user!!)
                Log.d("search", "$userList")
            }
            .addOnFailureListener { exception ->
                _memberList.value = Resource.Error(exception.message ?: "An error occurred")
            }
    }

    fun addMember(addMember: String, board: String) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(board)
            .get()
            .addOnSuccessListener {
                val assignedToList = it.toObject(Board::class.java)?.assignedTo ?: mutableListOf()
                if (!assignedToList.contains(addMember)) {
                    assignedToList.add(addMember)
                    db.collection(Constant.BOARD_COLLECTION)
                        .document(board)
                        .update("assignedTo", assignedToList)
                        .addOnSuccessListener {
                            _added.value = Resource.Success("added")
                        }
                        .addOnFailureListener {
                            _added.value = Resource.Error("error adding member")
                        }

                }

            }
    }
}