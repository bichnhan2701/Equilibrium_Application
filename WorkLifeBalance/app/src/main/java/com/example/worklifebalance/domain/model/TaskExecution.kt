package com.example.worklifebalance.domain.model

data class TaskExecution(
    val id: Long = 0,
    val taskId: String,
    val executionDate: Long
)

