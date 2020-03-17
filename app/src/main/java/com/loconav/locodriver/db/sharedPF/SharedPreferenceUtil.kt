package com.loconav.locodriver.db.sharedPF

import android.app.Activity
import android.content.SharedPreferences
import com.google.gson.Gson
import com.loconav.locodriver.application.LocoDriverApplication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SharedPreferenceUtil(val fileName: String) : KoinComponent {

    val gson : Gson by inject()

    private val sharedPreferences: SharedPreferences =
        LocoDriverApplication.instance.applicationContext.getSharedPreferences(
            fileName, Activity.MODE_PRIVATE
        )


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
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = gson.toJson(`object`)
        //Save that String in SharedPreferences
        saveData(key, jsonString)
    }

    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value : String = getData(key, "")
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return try {
            gson.fromJson(value, T::class.java)
        } catch (exception : Exception) {
            null
        }
    }



    fun deleteAllData() {
        editor.clear()
        editor.apply()
    }

}