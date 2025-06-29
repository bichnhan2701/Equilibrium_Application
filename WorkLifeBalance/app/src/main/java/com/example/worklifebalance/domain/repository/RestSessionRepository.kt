package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.RestSession

interface RestSessionRepository {
    suspend fun addRestSession(restSession: RestSession): Long
    suspend fun getRestSessionHistory(): List<RestSession>
    suspend fun getTotalRestMinutes(): Int
}

