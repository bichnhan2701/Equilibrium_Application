package com.example.worklifebalance.domain.usecase.goal

import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.repository.GoalRepository
import javax.inject.Inject

class GetAllGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(): List<Goal> = repository.getAllGoals()
}
