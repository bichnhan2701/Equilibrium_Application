package com.example.worklifebalance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worklifebalance.data.local.entity.RestSessionEntity

@Dao
interface RestSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestSession(restSession: RestSessionEntity): Long

    @Query("SELECT * FROM rest_session ORDER BY id DESC")
    suspend fun getAllRestSessions(): List<RestSessionEntity>

    @Query("SELECT SUM(total_rest_minutes) FROM rest_session")
    suspend fun getTotalRestMinutes(): Int?
}

