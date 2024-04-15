package com.example.projectmanagement.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectmanagement.R
import com.example.projectmanagement.uitl.LocalData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  localData:LocalData
    private lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        localData = LocalData(applicationContext)
        auth = FirebaseAuth.getInstance()
        localData.setCurrentUserId(auth.currentUser?.uid)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.chatSearch -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                }


            }
        }

        if (auth.currentUser != null) {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment)
        }
        else {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.introFragment)
        }
    }
    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.homeFragment) {
            finish()
        }
        else{
            super.onBackPressed()
        }
    }
    private fun toast(result: String?) {
        Toast.makeText(applicationContext, result, Toast.LENGTH_LONG)?.show()
    }
}
