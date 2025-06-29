package com.example.worklifebalance.domain.usecase.task

import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksByGoalIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(goalId: String): List<Task> = repository.getAllTasks().filter { it.goalId == goalId }
}

