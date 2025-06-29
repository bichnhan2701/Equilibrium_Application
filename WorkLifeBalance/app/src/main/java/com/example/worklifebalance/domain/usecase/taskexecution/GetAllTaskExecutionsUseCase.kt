package com.example.worklifebalance.domain.usecase.taskexecution

import com.example.worklifebalance.domain.model.TaskExecution
import com.example.worklifebalance.domain.repository.TaskExecutionRepository
import javax.inject.Inject

class GetAllTaskExecutionsUseCase @Inject constructor(
    private val repository: TaskExecutionRepository
) {
    suspend operator fun invoke(): List<TaskExecution> = repository.getAllTaskExecutions()
}

