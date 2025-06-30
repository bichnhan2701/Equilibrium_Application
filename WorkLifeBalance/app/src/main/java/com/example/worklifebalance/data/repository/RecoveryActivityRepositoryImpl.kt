package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.RecoveryActivityDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.RecoveryActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecoveryActivityRepositoryImpl @Inject constructor(
    private val dao: RecoveryActivityDao
) : RecoveryActivityRepository {
    override suspend fun insertRecoveryActivity(activity: RecoveryActivity) {
        dao.insertRecoveryActivity(activity.toEntity())
    }

    override fun getAllRecoveryActivities(): Flow<List<RecoveryActivity>> {
        return dao.getAllRecoveryActivities().map { list ->
            list.map { it.toDomain() }
        }
    }
}

