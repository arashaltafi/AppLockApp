package com.arash.altafi.applockapp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesPass {

    companion object {
        private const val PASS_CLASS = "pass_class"
        private const val LOCK_CLASS = "lock_class"
        private const val MY_PASS_CLASS = "my_pass_class"
    }
    private var preferences: SharedPreferences? = null

    fun sharedPrefPass(context: Context) {
        preferences = context.getSharedPreferences(MY_PASS_CLASS, Context.MODE_PRIVATE)
    }

    fun savePass(pass: String?, lock: Boolean) {
        val editor: SharedPreferences.Editor = preferences!!.edit()
        editor.putString(PASS_CLASS, pass)
        editor.putBoolean(LOCK_CLASS, lock)
        editor.apply()
    }

    fun getPass(): String {
        return preferences?.getString(PASS_CLASS, null) ?: ""

    }

    fun getLock(): Boolean {
        return preferences?.getBoolean(LOCK_CLASS, false) ?: false
    }

    fun clear() {
        val editor: SharedPreferences.Editor = preferences!!.edit()
        editor.clear()
        editor.apply()
    }

    fun remove() {
        val editor: SharedPreferences.Editor = preferences!!.edit()
        editor.remove(PASS_CLASS)
        editor.apply()
    }

}