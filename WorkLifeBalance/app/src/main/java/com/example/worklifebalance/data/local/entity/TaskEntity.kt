package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    val id: String = "",
    val domainId: String = "",
    val goalId: String = "",
    val name: String = "",
    val description: String? = null,
    val difficulty: String = "",
    val taskType: String = "",
    val plannedDates: List<Long> = emptyList(),
    val plannedTime: String = "",
    val repeatRule: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", null, "", "", emptyList(), "", "", 0L, 0L)
}
