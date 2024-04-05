package com.example.projectmanagement.ui.fragment
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentHomeBinding
import com.example.projectmanagement.model.User
import com.example.projectmanagement.uitl.LocalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentHomeBinding? = null
    private lateinit var localData: LocalData
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

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val hamburgerButton = toolbar.findViewById<ImageButton>(R.id.btn_hamburger)
        val drawerLayout = binding.drawerLayout
        hamburgerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navProfilePic = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.image_profile)
        val navTitle = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nav_username)
        val logout = binding.navView.menu.findItem(R.id.logout)
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
}
