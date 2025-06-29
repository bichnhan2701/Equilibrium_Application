package com.example.worklifebalance.domain.usecase.rest

import com.example.worklifebalance.domain.model.RestSession
import com.example.worklifebalance.domain.repository.RestSessionRepository
import javax.inject.Inject

class AddRestSessionUseCase @Inject constructor(
    private val repository: RestSessionRepository
) {
    suspend operator fun invoke(restSession: RestSession): Long {
        return repository.addRestSession(restSession)
    }
}

