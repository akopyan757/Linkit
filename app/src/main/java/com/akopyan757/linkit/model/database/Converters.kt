package com.akopyan757.linkit.model.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(";").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ";")
    }


    @TypeConverter
    fun fromStringToInts(intsListString: String): List<Int> {
        return if (intsListString.isNotEmpty())
            intsListString.split(";").map { it.toInt() }
                else
            emptyList()
    }

    @TypeConverter
    fun intsToString(ints: List<Int>): String {
        return ints.joinToString(separator = ";")
    }

}