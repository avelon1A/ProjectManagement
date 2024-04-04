package com.example.projectmanagement.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivityHomeBinding
import com.example.projectmanagement.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val hamburgerButton = toolbar.findViewById<ImageButton>(R.id.btn_hamburger)
        val drawerLayout = binding.drawerLayout
        hamburgerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navProfilePic = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.image_profile)
        val navTitle = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nav_username)
        fetchUserDetails{
            navTitle.text = it.username
            Glide.with(this).load(it.imageURL).into(navProfilePic)
            Log.d("user", "${it}")
        }
        binding.fab.setOnClickListener {

        }
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
    }}
