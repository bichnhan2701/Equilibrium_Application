package com.example.worklifebalance.domain.usecase.goal

import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.repository.GoalRepository
import javax.inject.Inject

class GetGoalByIdUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(id: String): Goal? = repository.getGoalById(id)
}