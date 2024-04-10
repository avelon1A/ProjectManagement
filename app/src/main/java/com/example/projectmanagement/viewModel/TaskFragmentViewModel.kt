package com.example.projectmanagement.viewModel

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TaskFragmentViewModel:ViewModel() {

    val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    // MutableStateFlow to emit the current board state
    private var _register = MutableStateFlow<Resource<Board>>(Resource.unSpecified())
    val response: Flow<Resource<Board>> = _register


    fun saveTaskInfo(id:String,task:Task) {
        val taksListHashMap = HashMap<String,Any>()
        taksListHashMap[Constant.TASK_LIST] = task
        db.collection(Constant.BOARD_COLLECTION)
            .document(id)
            .update(taksListHashMap)
            .addOnSuccessListener { documentReference ->
              Log.d("update","$documentReference")
            }
            .addOnFailureListener { e ->
                Log.d("update","$e")
            }
    }
     fun getBoardById(id: String) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)

                    _register.value = Resource.Success(board!!)
                    Log.d("rv_list","${board}")

            }
            .addOnFailureListener { e ->
                Log.e("TaskFragmentViewModel", "Error getting board", e)
                // Emit null if retrieval fails
                _register.value = Resource.Error(e.toString())
            }
    }

}