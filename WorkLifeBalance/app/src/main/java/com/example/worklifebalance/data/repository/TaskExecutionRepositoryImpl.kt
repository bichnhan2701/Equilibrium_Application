package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.TaskExecutionDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.TaskExecution
import com.example.worklifebalance.domain.repository.TaskExecutionRepository
import javax.inject.Inject

class TaskExecutionRepositoryImpl @Inject constructor(
    private val dao: TaskExecutionDao
) : TaskExecutionRepository {
    override suspend fun insertExecution(execution: TaskExecution) {
        dao.insertExecution(execution.toEntity())
    }

    override suspend fun getExecutionsForTask(taskId: String): List<TaskExecution> {
        return dao.getExecutionsForTask(taskId).map { it.toDomain() }
    }

    override suspend fun getAllTaskExecutions(): List<TaskExecution> {
        return dao.getAllTaskExecutions().map { it.toDomain() }
    }

}
