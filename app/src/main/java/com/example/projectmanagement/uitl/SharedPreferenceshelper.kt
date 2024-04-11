package com.example.projectmanagement.uitl

import com.google.firebase.firestore.auth.User

interface  SharedPreferenceshelper {

    fun setCurrentUser(user: String?)
    fun getCurrentuser():String?

    fun setCurrentUserId(userid:String?)
    fun getCurrentUserId():String?

}
