package com.example.equilibrium.domain.repository

import android.content.Context

class PreferencesRepository(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("equilibrium_prefs", Context.MODE_PRIVATE)

    fun setSeenOnboarding() {
        sharedPrefs.edit().putBoolean("seen_onboarding", true).apply()
    }

    fun hasSeenOnboarding(): Boolean {
        return sharedPrefs.getBoolean("seen_onboarding", false)
    }
}