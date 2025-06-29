package com.example.worklifebalance.domain.usecase.rest

import com.example.worklifebalance.domain.model.RestSession
import com.example.worklifebalance.domain.repository.RestSessionRepository
import javax.inject.Inject

class GetRestSessionHistoryUseCase @Inject constructor(
    private val repository: RestSessionRepository
) {
    suspend operator fun invoke(): List<RestSession> {
        return repository.getRestSessionHistory()
    }
}

