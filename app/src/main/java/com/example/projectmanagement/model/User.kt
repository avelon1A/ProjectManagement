package com.example.projectmanagement.model

data class User(
    val username:String,
    val email:String,
    val password:String,
    val imageURL: String = "",
    val uid:String = ""
)
