package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.RestSessionDao
import com.example.worklifebalance.domain.mapper.RestSessionMapper
import com.example.worklifebalance.domain.model.RestSession
import com.example.worklifebalance.domain.repository.RestSessionRepository
import javax.inject.Inject

class RestSessionRepositoryImpl @Inject constructor(
    private val dao: RestSessionDao
) : RestSessionRepository {
    override suspend fun addRestSession(restSession: RestSession): Long {
        return dao.insertRestSession(RestSessionMapper.toEntity(restSession))
    }

    override suspend fun getRestSessionHistory(): List<RestSession> {
        return dao.getAllRestSessions().map { RestSessionMapper.fromEntity(it) }
    }

    override suspend fun getTotalRestMinutes(): Int {
        return dao.getTotalRestMinutes() ?: 0
    }
}

