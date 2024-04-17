package com.example.projectmanagement.uitl

import User

interface  SharedPreferenceshelper {

    fun setCurrentUser(user: String?)
    fun getCurrentuser():String?

    fun setCurrentUserId(userid:String?)
    fun getCurrentUserId():String?

    fun saveAssignedTo(assignTo:ArrayList<String>?)
    fun getAssignedTO():ArrayList<String>?

    fun saveSearch(assignTo:ArrayList<User>?)
    fun getSearch():ArrayList<User>?

}
