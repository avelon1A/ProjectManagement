package com.example.projectmanagement.viewModel

import Resource
import User
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatSearchViewModel:ViewModel() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _chatResult = MutableStateFlow<Resource<ArrayList<User>>>(Resource.unSpecified())
    val chatResult: StateFlow<Resource<ArrayList<User>>> = _chatResult

    fun search(username: String) {
        _chatResult.value = Resource.Loading()

        db.collection(Constant.USER_COLLECTION)
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userList = ArrayList<User>()
                var user = User()
                for (document in querySnapshot.documents){
                    user = document.toObject(User::class.java)!!
                }
                userList.add(user)
                _chatResult.value = Resource.Success(userList!!)
                Log.d("search", "$userList")
            }
            .addOnFailureListener { exception ->
                _chatResult.value = Resource.Error(exception.message ?: "An error occurred")
            }
    }

}