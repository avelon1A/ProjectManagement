package com.example.projectmanagement.uitl

import android.content.Context
import android.content.SharedPreferences

class LocalData(context:Context):SharedPreferenceshelper {
    private val preferences: SharedPreferences = context.getSharedPreferences("LocalData", Context.MODE_PRIVATE)
    override fun setCurrentUser(currentuser:String?) {
        preferences?.edit()?.putString("CurrentUser", currentuser)?.apply()
    }

    override fun getCurrentuser(): String? {
        return preferences?.getString("CurrentUser", null)
    }

    override fun setCurrentUserId(userid: String?) {
        preferences?.edit()?.putString("CurrentUserId", userid)?.apply()

    }

    override fun getCurrentUserId(): String? {
        return preferences?.getString("CurrentUserId", null)

    }


}