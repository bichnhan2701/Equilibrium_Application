package com.example.worklifebalance.domain.usecase.task

import com.example.worklifebalance.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTasksByDomainIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(domainId: String) {
        repository.deleteTasksByDomainId(domainId)
    }
}

