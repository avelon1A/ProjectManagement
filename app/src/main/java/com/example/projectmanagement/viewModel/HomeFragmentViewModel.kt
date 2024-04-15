import androidx.lifecycle.ViewModel
import com.example.projectmanagement.Constant
import com.example.projectmanagement.model.Board
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeFragmentViewModel : ViewModel() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val currentUserId = mAuth.currentUser?.uid

    private fun getCurrentUserID(): String? {
        return currentUserId
    }
    private val _board = MutableStateFlow<Resource<ArrayList<Board>>>(Resource.unSpecified())
    val board: StateFlow<Resource<ArrayList<Board>>> = _board.asStateFlow()
    init {
        getBoard()
    }

    private fun getBoard() {
        db.collection(Constant.BOARD_COLLECTION)
            .whereArrayContains(Constant.ASSIGNED_TO, getCurrentUserID()!!)
            .addSnapshotListener { documents, error ->
                if (error != null) {
                    _board.value = Resource.Error("Unable to load boardList")
                    return@addSnapshotListener
                }

                val boardList: ArrayList<Board> = ArrayList()
                documents?.forEach { document ->
                    val board = document.toObject(Board::class.java)
                    board.id = document.id
                    boardList.add(board)
                }

                _board.value = Resource.Success(boardList)
            }
    }
}
