package com.example.projectmanagement.ui.fragment
import HomeFragmentViewModel
import Resource
import User
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.adapter.BoardAdapter
import com.example.projectmanagement.adapter.BoardItemClickListener
import com.example.projectmanagement.databinding.FragmentHomeBinding
import com.example.projectmanagement.model.Board
import com.example.projectmanagement.uitl.LocalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), BoardItemClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentHomeBinding? = null
    private lateinit var localData: LocalData
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BoardAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        localData = LocalData(requireContext())
        val navController = view.findNavController()
        viewModel = HomeFragmentViewModel()

        recyclerView = binding.recyclerView
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val hamburgerButton = toolbar.findViewById<ImageButton>(R.id.btn_hamburger)
        val drawerLayout = binding.drawerLayout
        hamburgerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navProfilePic = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.image_profile)
        val navTitle = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nav_username)
        val logout = binding.navView.menu.findItem(R.id.logout)
        val usernameTitle = binding.navView.menu.findItem(R.id.myProfile)
        usernameTitle.setOnMenuItemClickListener {

            true
        }
       logout.setOnMenuItemClickListener {
           showLogoutConfirmationDialog()
           true
        }

        fetchUserDetails {
            navTitle.text = it.username
            Glide.with(this).load(it.imageURL).into(navProfilePic)
            Log.d("user", "${it}")
            localData.setCurrentUser(it.username)

        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createBoard)
        }

        lifecycleScope.launch(){
            viewModel.board.collect{
                when(it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {

                        Log.d("rv_data","${it.data}")
                        adapter = BoardAdapter(requireContext(), it.data!!, this@HomeFragment)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                    else -> {

                    }
                }
            }
        }


    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_homeFragment_to_introFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchUserDetails(callback: (User) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            localData.setCurrentUserId(userId)

            firestore.collection("user").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: ""
                        val profilePicUrl = document.getString("imageURL") ?: ""
                        val email = document.getString("email") ?: ""

                        val user = User(username = username, email = email, password = "", imageURL = profilePicUrl)
                        callback(user)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("error", "$exception")
                }
        }
    }


    override fun onItemClick(boardId: String, model: Board) {
        val action = HomeFragmentDirections.actionHomeFragmentToBoardFragment(model.id,model.name)
       findNavController().navigate(action)
    }
}
