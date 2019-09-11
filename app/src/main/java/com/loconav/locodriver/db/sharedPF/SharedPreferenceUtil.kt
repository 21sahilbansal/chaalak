package com.loconav.locodriver.db.sharedPF

import android.app.Activity
import android.content.SharedPreferences
import com.loconav.locodriver.application.LocoDriverApplication

object SharedPreferenceUtil {

    private val DEFAULT_PREF_NAME = "locodrive_prefs"

    private val sharedPreferences : SharedPreferences = LocoDriverApplication.instance.applicationContext.getSharedPreferences(
        DEFAULT_PREF_NAME, Activity.MODE_PRIVATE)


    private val editor = sharedPreferences.edit()


    fun saveData(key: String, value: String) {
        editor.putString(key, value)
        return editor.apply()
    }


    fun saveData(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Long) {
        editor.putLong(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Float) {
        editor.putFloat(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Int) {
        editor.putInt(key, value)
        return editor.apply()
    }


    fun getData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)?:""
    }

    fun deleteAllData() {
        editor.clear()
        editor.apply()
    }


}