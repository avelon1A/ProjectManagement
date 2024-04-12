package com.example.projectmanagement.ui.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.example.projectmanagement.R

class CustomLoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_loading)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)

        val animationView = findViewById<LottieAnimationView>(R.id.aaaa)
        animationView.playAnimation()
        animationView.setBackgroundColor(Color.TRANSPARENT)
        animationView.loop(true)
    }
}
