package com.example.worklifebalance.data.local.dao

import androidx.room.*
import com.example.worklifebalance.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("DELETE FROM task")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: String): TaskEntity?
}