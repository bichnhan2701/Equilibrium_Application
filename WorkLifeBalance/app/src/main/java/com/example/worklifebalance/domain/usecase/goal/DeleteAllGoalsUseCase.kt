package com.example.worklifebalance.domain.usecase.goal

import com.example.worklifebalance.domain.repository.GoalRepository
import javax.inject.Inject

class DeleteAllGoalsUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke() {
        goalRepository.deleteAllGoals()
    }
}
