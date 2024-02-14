package com.jamesellerbee.taskfireandroid.dal.settings

import android.app.Activity
import android.content.Context

class AppSettings(activity: Activity) {
    private val preferences = activity.getPreferences(Context.MODE_PRIVATE)

    companion object {
        const val rememberCredentials = "rememberCredentials"
        const val savedUsername = "savedUsername"
        const val savedPassword = "savedPassword"
    }

    fun get(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue)!!
    }

    fun set(key: String, value: String) {
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun remove(key: String) {
        with(preferences.edit()) {
            remove(key)
            apply()
        }
    }
}