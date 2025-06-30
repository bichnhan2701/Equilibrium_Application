package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.Task

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAllTasks()
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun deleteTasksByGoalId(goalId: String)
    suspend fun deleteTasksByDomainId(domainId: String)
}
