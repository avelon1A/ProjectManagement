package com.example.projectmanagement.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.projectmanagement.R

fun CustomColorPickerDialog(context: Context,onColorSelected:(Int)-> Unit){
    val dialogView = LayoutInflater.from(context).inflate(R.layout.color_picker,null)
    val alertDialogBuilder = AlertDialog.Builder(context)
        .setView(dialogView )
        .setCancelable(true)

    val dialog = alertDialogBuilder.create()

    dialogView.findViewById<View>(R.id.color1).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color1))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color2).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color2))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color3).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color3))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color4).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color4))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color5).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color5))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color6).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color6))
        dialog.dismiss()
    }
    dialogView.findViewById<View>(R.id.color7).setOnClickListener {
        onColorSelected.invoke(ContextCompat.getColor(context, R.color.Color7))
        dialog.dismiss()
    }

    dialog.dismiss()

}