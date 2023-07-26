package com.wholesale.jewels.fauxiq.baheekhata.enums

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class ArrayListConverter {

    companion object {
        @TypeConverter
        fun fromString(value: String): ArrayList<String> {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }
    }

    fun getfromString(fromString: String) = fromString

}



