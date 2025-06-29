package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.TaskExecution


interface TaskExecutionRepository {
    suspend fun insertExecution(execution: TaskExecution)
    suspend fun getExecutionsForTask(taskId: String): List<TaskExecution>
    suspend fun getAllTaskExecutions(): List<TaskExecution>
}
