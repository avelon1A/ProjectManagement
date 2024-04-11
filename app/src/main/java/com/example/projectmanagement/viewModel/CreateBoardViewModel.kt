package com.example.projectmanagement.viewModel

import Resource
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.uitl.LocalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class CreateBoardViewModel :ViewModel(){
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage:FirebaseStorage = FirebaseStorage.getInstance()
    private var _creation = MutableStateFlow<Resource<Board>>(Resource.unSpecified())
    val response: Flow<Resource<Board>> = _creation
    private val getcurrentUserID = mAuth.currentUser?.uid
fun createBoard(name: String,currentUserName:String, image: Uri) {
    val assignedUserArrayList: ArrayList<String> = ArrayList()

    assignedUserArrayList.add(getcurrentUserID!!)
    val boardId = UUID.randomUUID().toString()

    val imageName = UUID.randomUUID().toString()
    val imageRef: StorageReference = storage.reference.child("board_images/$imageName")
    imageRef.putFile(image)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val board = Board(
                    id = boardId,
                    name = name,
                    createdBy = currentUserName,
                    image = imageUrl,
                    assignedTo = assignedUserArrayList,
                     task = ArrayList()
                )
                saveBoardInfo(board)
            }.addOnFailureListener { e ->
                _creation.value = Resource.Error("Failed to get download URL: ${e.message}")
            }
        }
        .addOnFailureListener { e ->
            _creation.value = Resource.Error("Image upload failed: ${e.message}")
        }
}

    private fun saveBoardInfo(board: Board) {
        db.collection(Constant.BOARD_COLLECTION)
            .add(board)
            .addOnSuccessListener { documentReference ->
                val updatedBoard = board.copy(id = documentReference.id)
                _creation.value = Resource.Success(updatedBoard)
            }
            .addOnFailureListener { e ->
                _creation.value = Resource.Error("Failed to save board: ${e.message}")
            }
    }
    private fun getCurrentUserID():String?{
        return getcurrentUserID
    }
}