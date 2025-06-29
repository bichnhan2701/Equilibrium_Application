package com.example.worklifebalance.data.local.dao

import androidx.room.*
import com.example.worklifebalance.data.local.entity.GoalEntity

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM goal")
    suspend fun getAllGoals(): List<GoalEntity>

    @Query("SELECT * FROM goal WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: String): GoalEntity?

    @Query("DELETE FROM goal")
    suspend fun deleteAllGoals()
}