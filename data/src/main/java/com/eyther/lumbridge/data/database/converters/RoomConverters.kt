package com.eyther.lumbridge.data.database.converters

import android.util.Log
import androidx.room.TypeConverter
import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RoomConverters {
    /**
     * Unfortunately, I'm not sure how to inject this Gson instance into this class as Room needs
     * to be able to create an instance of this class without any parameters. I'm going to leave
     * this as is for now. /shrug
     */
    private val gson: Gson = Gson()

    @TypeConverter
    fun fromStringToGroceriesListEntry(value: String?): ArrayList<GroceriesListEntryEntity> {
        val listType: Type = object : TypeToken<ArrayList<GroceriesListEntryEntity?>?>() {}.type
        Log.d("RoomConverters", "fromStringToGroceriesListEntry: $value")
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromGroceriesListEntryToString(list: ArrayList<GroceriesListEntryEntity?>?): String {
        return gson.toJson(list)
    }
}
