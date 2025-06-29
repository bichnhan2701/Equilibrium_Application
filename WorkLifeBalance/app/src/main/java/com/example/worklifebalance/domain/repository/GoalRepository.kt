// GoalRepository.kt
package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.Goal

interface GoalRepository {
    suspend fun insertGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)
    suspend fun deleteAllGoals()
    suspend fun getAllGoals(): List<Goal>
    suspend fun getGoalById(id: String): Goal?
}