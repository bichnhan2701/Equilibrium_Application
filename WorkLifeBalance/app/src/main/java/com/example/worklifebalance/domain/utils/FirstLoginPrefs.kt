package com.example.worklifebalance.domain.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object FirstLoginPrefs {
    private const val PREFS_NAME = "sync_prefs"
    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isFirstLogin(context: Context, userId: String): Boolean =
        getPrefs(context).getBoolean("isFirstLogin_$userId", true)

    fun setFirstLogin(context: Context, userId: String, isFirst: Boolean) {
        getPrefs(context).edit { putBoolean("isFirstLogin_$userId", isFirst) }
    }
}

