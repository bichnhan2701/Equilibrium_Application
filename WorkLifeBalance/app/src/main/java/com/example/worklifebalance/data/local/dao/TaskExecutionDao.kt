package com.example.worklifebalance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.worklifebalance.data.local.entity.TaskExecutionEntity

@Dao
interface TaskExecutionDao {
    @Insert
    suspend fun insertExecution(execution: TaskExecutionEntity)

    @Query("SELECT * FROM task_execution WHERE taskId = :taskId ORDER BY executionDate DESC")
    suspend fun getExecutionsForTask(taskId: String): List<TaskExecutionEntity>

    @Query("SELECT * FROM task_execution ORDER BY executionDate DESC")
    suspend fun getAllTaskExecutions(): List<TaskExecutionEntity>
}
