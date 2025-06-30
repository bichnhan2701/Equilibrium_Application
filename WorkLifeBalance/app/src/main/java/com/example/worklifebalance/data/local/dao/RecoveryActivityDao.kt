package com.example.worklifebalance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worklifebalance.data.local.entity.RecoveryActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecoveryActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecoveryActivity(activity: RecoveryActivityEntity)

    @Query("SELECT * FROM recovery_activities")
    fun getAllRecoveryActivities(): Flow<List<RecoveryActivityEntity>>
}
