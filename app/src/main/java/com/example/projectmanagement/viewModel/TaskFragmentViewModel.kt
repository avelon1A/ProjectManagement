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
    private var _register = MutableStateFlow<Resource<Board>>(Resource.unSpecified())
    val response: Flow<Resource<Board>> = _register


    fun saveTaskInfo(id: String, task: Task) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)
                if (board != null) {
                    val tasks = board.task // Get the current list of tasks
                    tasks.add(task) // Add the new task to the list
                    val updatedData = mapOf(
                        Constant.TASK_LIST to tasks
                    )
                    db.collection(Constant.BOARD_COLLECTION)
                        .document(id)
                        .update(updatedData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("update", "$documentReference")
                        }
                        .addOnFailureListener { e ->
                            Log.e("update", e.message ?: "Unknown error")
                        }
                } else {
                    Log.e("saveTaskInfo", "Board not found for ID: $id")
                }
            }
            .addOnFailureListener { e ->
                Log.e("saveTaskInfo", "Error getting board document", e)
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