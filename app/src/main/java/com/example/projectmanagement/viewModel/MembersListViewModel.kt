package com.example.projectmanagement.viewModel

import Resource
import User
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MembersListViewModel: ViewModel() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _memberList = MutableStateFlow<Resource<ArrayList<User>>>(Resource.unSpecified())
    val memberList: Flow<Resource<ArrayList<User>>> = _memberList


    fun getMembersList(assignedTo: ArrayList<String>) {
        if (assignedTo.isNotEmpty()) {
            db.collection(Constant.USER_COLLECTION)
                .whereIn(Constant.ID, assignedTo)
                .get()
                .addOnSuccessListener { document ->
                    val userList: ArrayList<User> = ArrayList()
                    for(i in document.documents){
                        val user = i.toObject(User::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    _memberList.value = Resource.Success(userList)
                }
                .addOnFailureListener {
                    _memberList.value = Resource.Error("Unable to load memberList: $it")
                }
        } else {
            _memberList.value = Resource.Error("AssignedTo list is empty")
        }
    }
}


