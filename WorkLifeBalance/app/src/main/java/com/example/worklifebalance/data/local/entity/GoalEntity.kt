package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal")
data class GoalEntity(
    @PrimaryKey
    val id: String = "",
    val domainId: String = "",
    val name: String = "",
    val description: String? = null,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", null, 0L, 0L, 0L, 0L)
}
