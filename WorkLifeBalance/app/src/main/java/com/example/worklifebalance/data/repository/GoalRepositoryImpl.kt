// GoalRepositoryImpl.kt
package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.GoalDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.repository.GoalRepository
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal.toEntity())
    }
    override suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal.toEntity())
    }
    override suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal.toEntity())
    }
    override suspend fun getAllGoals(): List<Goal> {
        return goalDao.getAllGoals().map { it.toDomain() }
    }
    override suspend fun deleteAllGoals() {
        goalDao.deleteAllGoals()
    }
    override suspend fun getGoalById(id: String): Goal? {
        return goalDao.getGoalById(id)?.toDomain()
    }
}