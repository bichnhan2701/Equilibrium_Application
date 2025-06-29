package com.example.worklifebalance.data.local.converter

import androidx.room.TypeConverter
import kotlin.collections.joinToString
import kotlin.collections.mapNotNull
import kotlin.text.isNullOrEmpty
import kotlin.text.split
import kotlin.text.toLongOrNull

class ListLongConverter {
    @TypeConverter
    fun fromListLong(list: List<Long>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toListLong(data: String?): List<Long> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(",").mapNotNull { it.toLongOrNull() }
    }
}


