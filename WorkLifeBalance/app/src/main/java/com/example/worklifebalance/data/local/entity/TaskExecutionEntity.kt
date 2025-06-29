package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_execution")
data class TaskExecutionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: String = "",
    val executionDate: Long = 0L // timestamp (milliseconds)
) {
    // No-argument constructor for Firebase
    constructor() : this(0, "", 0L)
}
