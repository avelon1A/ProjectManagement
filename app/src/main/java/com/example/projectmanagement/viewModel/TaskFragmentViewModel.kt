package com.example.projectmanagement.viewModel

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskFragmentViewModel : ViewModel() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _register = MutableStateFlow<Resource<Board>>(Resource.unSpecified())
    val response: Flow<Resource<Board>> = _register
    private var _saveTask = MutableStateFlow<Resource<String>>(Resource.unSpecified())
    val saveTask: Flow<Resource<Board>> = _register

    fun saveTaskInfo(id: String, task: Task) {
        viewModelScope.launch {
            _saveTask.emit(Resource.Loading())
        }


        db.collection(Constant.BOARD_COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)
                if (board != null) {
                    val tasks = board.task
                    tasks.add(task)
                    val updatedData = mapOf(
                        Constant.TASK_LIST to tasks
                    )
                    db.collection(Constant.BOARD_COLLECTION)
                        .document(id)
                        .update(updatedData)
                        .addOnSuccessListener { documentReference ->
                            viewModelScope.launch {
                                _saveTask.emit(Resource.Success("done"))
                                Log.d("update", "$documentReference")
                            }
                        }
                        .addOnFailureListener { e ->
                            viewModelScope.launch {
                                _saveTask.emit(Resource.Error("error"))
                                Log.e("update", e.message ?: "Unknown error")
                            }
                        }
                } else {
                    Log.e("saveTaskInfo", "Board not found for ID: $id")
                }
            }
            .addOnFailureListener { e ->
                Log.e("saveTaskInfo", "Error getting board document", e)
            }


    }

    fun getBoardByIdAndListen(id: String) {
        viewModelScope.launch {
            _register.emit(Resource.Loading())
        }

        db.collection(Constant.BOARD_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("TaskFragmentViewModel", "Error listening to board changes", e)
                    viewModelScope.launch {
                        _register.value = Resource.Error(e.toString())
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val board = snapshot.toObject(Board::class.java)
                    viewModelScope.launch {
                        _register.value = Resource.Success(board!!)
                        Log.d("rv_list", "$board")
                    }
                } else {
                    Log.e("TaskFragmentViewModel", "Board document not found")
                    viewModelScope.launch {
                        _register.value = Resource.Error("Board document not found")
                    }
                }
            }
    }

}