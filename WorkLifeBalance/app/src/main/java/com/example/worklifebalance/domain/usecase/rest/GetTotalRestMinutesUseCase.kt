package com.example.worklifebalance.domain.usecase.rest

import com.example.worklifebalance.domain.repository.RestSessionRepository
import javax.inject.Inject

class GetTotalRestMinutesUseCase @Inject constructor(
    private val repository: RestSessionRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getTotalRestMinutes()
    }
}

