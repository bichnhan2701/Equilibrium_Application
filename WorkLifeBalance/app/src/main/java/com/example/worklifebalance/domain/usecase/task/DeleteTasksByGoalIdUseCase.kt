package com.example.worklifebalance.domain.usecase.task

import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTasksByGoalIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(goalId: String) {
        repository.deleteTasksByGoalId(goalId)
    }
}

