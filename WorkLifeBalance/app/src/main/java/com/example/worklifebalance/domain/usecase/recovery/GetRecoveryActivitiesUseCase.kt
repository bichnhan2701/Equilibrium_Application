package com.example.worklifebalance.domain.usecase.recovery

import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.data.repository.RecoveryActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecoveryActivitiesUseCase @Inject constructor(
    private val repository: RecoveryActivityRepository
) {
    operator fun invoke(): Flow<List<RecoveryActivity>> = repository.getAllRecoveryActivities()
}

