package com.example.projectmanagement.viewModel

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HomeFragmentViewModel:ViewModel() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var _board = MutableStateFlow<Resource<ArrayList<Board>>>(Resource.unSpecified())
    val boardResponse: Flow<Resource<ArrayList<Board>>> = _board
    private val getcurrentUserID = mAuth.currentUser?.uid

    private fun getCurrentUserID():String?{
        return getcurrentUserID
    }
    fun getBoard() {
        db.collection(Constant.BOARD_COLLECTION)
            .whereArrayContains(Constant.ASSIGNED_TO, getCurrentUserID()!!)
            .get()
            .addOnSuccessListener { documents ->
                val boardList: ArrayList<Board> = ArrayList()
                Log.d("rv_data","${documents}")
                for(i in documents.documents){
                    val board = i.toObject(Board::class.java)
                    if (board != null) {
                        board.id = i.id
                        boardList.add(board)
                    }
                }
                _board.value = Resource.Success(boardList)
            }
            .addOnFailureListener {
                _board.value = Resource.Error("Unable to load boardList")
            }
    }

}