package com.example.worklifebalance.domain.usecase.taskexecution

import com.example.worklifebalance.domain.model.TaskExecution
import com.example.worklifebalance.domain.repository.TaskExecutionRepository
import javax.inject.Inject

class InsertTaskExecutionUseCase @Inject constructor(
    private val repository: TaskExecutionRepository
) {
    suspend operator fun invoke(execution: TaskExecution) = repository.insertExecution(execution)
}

