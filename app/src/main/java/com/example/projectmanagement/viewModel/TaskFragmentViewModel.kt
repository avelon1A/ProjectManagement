package com.example.projectmanagement.viewModel

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.model.Card
import com.example.projectmanagement.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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

    fun updateTaskName(boardId: String, taskIndex: Int, newName: String) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(boardId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)
                if (board != null && taskIndex >= 0 && taskIndex < board.task.size) {
                    val updatedTasks = board.task.toMutableList()
                    updatedTasks[taskIndex].title = newName

                    db.collection(Constant.BOARD_COLLECTION)
                        .document(boardId)
                        .update("task", updatedTasks)
                        .addOnSuccessListener {
                            Log.d("updateTaskName", "Task name updated successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("updateTaskName", "Error updating task name", e)
                        }
                } else {
                    Log.e("updateTaskName", "Invalid board or task index")
                }
            }
            .addOnFailureListener { e ->
                Log.e("updateTaskName", "Error getting board document", e)
            }
    }

    fun deleteTask(boardId: String, position: Int) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(boardId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)
                if (board != null && position >= 0 && position < board.task.size) {
                    val updatedTasks = board.task.toMutableList()
                    updatedTasks.removeAt(position)

                    db.collection(Constant.BOARD_COLLECTION)
                        .document(boardId)
                        .update("task", updatedTasks)
                        .addOnSuccessListener {
                            Log.d("deleteTask", "Task deleted successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("deleteTask", "Error deleting task", e)
                        }
                } else {
                    Log.e("deleteTask", "Invalid board or task position")
                }
            }
            .addOnFailureListener { e ->
                Log.e("deleteTask", "Error getting board document", e)
            }
    }

    fun addCardToTask(boardId: String, taskIndex: Int, card: Card) {
        db.collection(Constant.BOARD_COLLECTION)
            .document(boardId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val board = documentSnapshot.toObject(Board::class.java)
                if (board != null && taskIndex >= 0 && taskIndex < board.task.size) {
                    val task = board.task[taskIndex]
                    task.cards.add(card)
                    val updatedTaskData = mapOf(
                        "task" to board.task
                    )

                    db.collection(Constant.BOARD_COLLECTION)
                        .document(boardId)
                        .update(updatedTaskData)
                        .addOnSuccessListener {
                            Log.d("addCardToTask", "Card added to task successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("addCardToTask", "Failed to add card to task", e)
                        }
                } else {
                    Log.e("addCardToTask", "Board or task index invalid")
                }
            }
            .addOnFailureListener { e ->
                Log.e("addCardToTask", "Error getting board document", e)
            }
    }


}