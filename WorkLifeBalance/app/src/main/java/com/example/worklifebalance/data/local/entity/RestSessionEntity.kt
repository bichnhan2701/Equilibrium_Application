package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "rest_session")
data class RestSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "total_rest_minutes") val totalRestMinutes: Int,
    @ColumnInfo(name = "rest_sessions") val restSessions: String // JSON string: list of {duration, time}
)

