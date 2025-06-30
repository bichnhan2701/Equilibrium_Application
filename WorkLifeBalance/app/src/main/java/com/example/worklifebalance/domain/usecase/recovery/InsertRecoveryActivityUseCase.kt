package com.example.worklifebalance.domain.usecase.recovery

import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.data.repository.RecoveryActivityRepository
import javax.inject.Inject

class InsertRecoveryActivityUseCase @Inject constructor(
    private val repository: RecoveryActivityRepository
) {
    suspend operator fun invoke(activity: RecoveryActivity) {
        repository.insertRecoveryActivity(activity)
    }
}

