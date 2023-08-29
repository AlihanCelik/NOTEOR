package com.example.notesapp

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromUriList(uriList: List<Uri>?): String? {
        return if (uriList == null) null else Gson().toJson(uriList.map { it.toString() })
    }

    @TypeConverter
    fun toUriList(uriListString: String?): List<Uri>? {
        return if (uriListString == null) null else {
            val uriStrings = Gson().fromJson<List<String>>(uriListString, object : TypeToken<List<String>>() {}.type)
            uriStrings.map { Uri.parse(it) }
        }
    }
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}