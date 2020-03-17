package com.loconav.locodriver.expense

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class DocumentTypeConverter : KoinComponent {
    val gson: Gson by inject()
    private val type = object : TypeToken<List<String>?>() {}.type

    @TypeConverter
    fun stringToDocument(json: String): List<String>? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun documentToString(nestedData: List<String>?): String {
        return gson.toJson(nestedData, type)
    }
}