package com.example.projectmanagement.uitl

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalData(context:Context):SharedPreferenceshelper {
    private val preferences: SharedPreferences = context.getSharedPreferences("LocalData", Context.MODE_PRIVATE)
    private val gson = Gson()
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

    override fun saveAssignedTo(assignTo: ArrayList<String>?) {
        val jsonString = gson.toJson(assignTo)
        preferences?.edit()?.putString("assigned_to", jsonString)?.apply()

    }

    override fun getAssignedTO(): ArrayList<String>? {
        val jsonString = preferences?.getString("assigned_to", null)
        return if (jsonString != null) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            null
        }
    }


}