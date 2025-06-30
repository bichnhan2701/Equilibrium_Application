package com.example.worklifebalance.data.repository

import com.example.worklifebalance.domain.model.RecoveryActivity
import kotlinx.coroutines.flow.Flow

interface RecoveryActivityRepository {
    suspend fun insertRecoveryActivity(activity: RecoveryActivity)
    fun getAllRecoveryActivities(): Flow<List<RecoveryActivity>>
}

