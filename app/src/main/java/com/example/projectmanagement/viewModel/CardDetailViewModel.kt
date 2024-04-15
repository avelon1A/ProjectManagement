package com.example.projectmanagement.viewModel

import Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CardDetailViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _deleteCard = MutableStateFlow<Resource<String>>(Resource.unSpecified())
    val deleteCard: StateFlow<Resource<String>> = _deleteCard

    private val _updateCard = MutableStateFlow<Resource<String>>(Resource.unSpecified())
    val updateCard: StateFlow<Resource<String>> = _updateCard
    fun deleteCard(boardId: String, taskIndex: Int, cardIndex: Int) {
        viewModelScope.launch {
            _deleteCard.value = Resource.Loading()

            try {
                val boardRef = db.collection(Constant.BOARD_COLLECTION).document(boardId).get().await()

                if (boardRef.exists()) {
                    val tasks = boardRef.toObject(Board::class.java)?.task
                    if (tasks != null && tasks.size > taskIndex) {
                        val taskList = tasks[taskIndex].cards
                        if (taskList != null && taskList.size > cardIndex) {
                            taskList.removeAt(cardIndex)
                            boardRef.reference.update("task", tasks).await()
                            _deleteCard.value = Resource.Success("deleted")
                        }
                    }
                } else {
                    _deleteCard.value = Resource.Error("Board not found")
                }
            } catch (e: Exception) {
                _deleteCard.value = Resource.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun updateCard(boardId: String, taskIndex: Int, cardIndex: Int, labelColor: String) {
        viewModelScope.launch {
            _updateCard.value = Resource.Loading()

            try {
                val boardRef = db.collection(Constant.BOARD_COLLECTION).document(boardId).get().await()

                if (boardRef.exists()) {
                    val tasks = boardRef.toObject(Board::class.java)?.task
                    if (tasks != null && tasks.size > taskIndex) {
                        val taskList = tasks[taskIndex].cards
                        if (taskList != null && taskList.size > cardIndex) {

                            taskList[cardIndex].labelColor = labelColor
                            boardRef.reference.update("task", tasks).await()
                            _updateCard.value = Resource.Success("Card updated")
                        }
                    }
                } else {
                    _updateCard.value = Resource.Error("Board not found")
                }
            } catch (e: Exception) {
                _updateCard.value = Resource.Error("An error occurred: ${e.message}")
            }
        }
    }
}
