package com.example.testDemo.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SavedSharedPreference {

    fun getPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setLogin(context: Context, login: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean("Login", login)
        editor.apply()
    }

    fun clearAllPreference(c: Context?) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.clear()
        e.apply()
    }

}