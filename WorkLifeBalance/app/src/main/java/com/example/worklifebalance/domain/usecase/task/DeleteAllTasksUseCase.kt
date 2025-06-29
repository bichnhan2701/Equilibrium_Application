package com.example.worklifebalance.domain.usecase.task

import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllTasks()
    }
}

