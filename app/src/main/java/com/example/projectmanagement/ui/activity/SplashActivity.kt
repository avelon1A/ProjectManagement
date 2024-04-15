package com.example.projectmanagement.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.projectmanagement.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 1500
    private val animationDuration: Long = 1500 // Duration to play the animation in milliseconds
    private lateinit var auth: FirebaseAuth
    private var splashAnimationView: LottieAnimationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()

        splashAnimationView = findViewById(R.id.splash_logo)
        splashAnimationView?.speed = 1.0f // Set the speed to normal

        CoroutineScope(Dispatchers.Main).launch {
            delay(splashTimeOut)
            splashAnimationView?.pauseAnimation() // Pause the animation after splashTimeOut milliseconds
            delay(animationDuration)
            navigateToMainActivity()
        }
    }

    private fun toast(result: String?) {
        Toast.makeText(applicationContext, result, Toast.LENGTH_LONG).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.form_right, R.anim.to_left)
    }
}
