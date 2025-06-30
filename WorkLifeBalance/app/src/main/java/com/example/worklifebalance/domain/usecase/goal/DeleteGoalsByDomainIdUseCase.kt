package com.example.worklifebalance.domain.usecase.goal

import com.example.worklifebalance.domain.repository.GoalRepository
import javax.inject.Inject

class DeleteGoalsByDomainIdUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(domainId: String) {
        repository.deleteGoalsByDomainId(domainId)
    }
}

