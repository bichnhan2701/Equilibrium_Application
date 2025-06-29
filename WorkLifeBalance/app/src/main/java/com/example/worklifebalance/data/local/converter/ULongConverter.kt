package com.example.worklifebalance.data.local.converter

import androidx.room.TypeConverter
import kotlin.toULong

class ULongConverter {
    @TypeConverter
    fun fromULong(value: ULong?): Long? = value?.toLong()

    @TypeConverter
    fun toULong(value: Long?): ULong? = value?.toULong()
}

