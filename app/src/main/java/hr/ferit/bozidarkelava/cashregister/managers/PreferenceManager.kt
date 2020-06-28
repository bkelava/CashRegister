package hr.ferit.bozidarkelava.cashregister.managers

import android.content.Context
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp

class PreferenceManager {
    companion object {
        const val PREFS_FILE="MyPreferences"
        const val PREFS_KEY_USERNAME="username"
    }

    fun saveUserEmail(email: String) {
        val sharedPreferences = CashRegisterApp.ApplicationContext.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        val sharedPreferences = CashRegisterApp.ApplicationContext.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "unknown")
    }

    fun saveUserId(userId: String) {
        val sharedPreferences = CashRegisterApp.ApplicationContext.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userid", userId)
        editor.apply()
    }

    fun getUserId(): String? {
        val sharedPreferences = CashRegisterApp.ApplicationContext.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE)
        return sharedPreferences.getString("userid", "unknown")
    }
}