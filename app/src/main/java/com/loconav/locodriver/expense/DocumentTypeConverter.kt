package com.loconav.locodriver.expense

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class DocumentTypeConverter {
    private val gson = Gson()
    private val type = object : TypeToken<List<String>?>() {

    }.type

    @TypeConverter
    fun stringToDocument(json: String): List<String>? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun documentToString(nestedData: List<String>?): String {
        return gson.toJson(nestedData, type)
    }
}