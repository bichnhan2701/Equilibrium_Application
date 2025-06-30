package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recovery_activities")
data class RecoveryActivityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val durationInMinutes: Int,
    val colorHex: ULong // Store color as hex value
)


