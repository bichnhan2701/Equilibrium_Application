package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "energy")
data class EnergyEntity(
    @PrimaryKey
    val id: String = "",
    val energy: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
) {
    // No-argument constructor for Firebase
    constructor() : this("", 0, System.currentTimeMillis())
}
