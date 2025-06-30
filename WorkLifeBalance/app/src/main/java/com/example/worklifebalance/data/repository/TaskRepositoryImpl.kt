package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.TaskDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }
    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }
    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { it.toDomain() }
    }
    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
    override suspend fun getTaskById(taskId: String): Task? {
        return taskDao.getTaskById(taskId)?.toDomain()
    }
    override suspend fun deleteTasksByGoalId(goalId: String) {
        taskDao.deleteTasksByGoalId(goalId)
    }
    override suspend fun deleteTasksByDomainId(domainId: String) {
        taskDao.deleteTasksByDomainId(domainId)
    }
}
