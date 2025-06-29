package com.example.worklifebalance.domain.usecase.task

import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.insertTask(task)
}

