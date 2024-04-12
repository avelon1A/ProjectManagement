package com.example.projectmanagement.uitl

interface  SharedPreferenceshelper {

    fun setCurrentUser(user: String?)
    fun getCurrentuser():String?

    fun setCurrentUserId(userid:String?)
    fun getCurrentUserId():String?

    fun saveAssignedTo(assignTo:ArrayList<String>?)
    fun getAssignedTO():ArrayList<String>?

}
