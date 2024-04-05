package com.example.projectmanagement.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.projectmanagement.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment)
        }
        else {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.introFragment)
        }

    }

    private fun toast(result: String?) {
        Toast.makeText(applicationContext, result, Toast.LENGTH_LONG)?.show()
    }


}
