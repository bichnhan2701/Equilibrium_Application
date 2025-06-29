package com.example.worklifebalance.domain.mapper

import com.example.worklifebalance.data.local.entity.RestSessionEntity
import com.example.worklifebalance.domain.model.RestSession
import com.example.worklifebalance.domain.model.RestSessionDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime

object RestSessionMapper {
    private val gson = Gson()

    fun fromEntity(entity: RestSessionEntity): RestSession {
        val type = object : TypeToken<List<RestSessionDetailJson>>() {}.type
        val details: List<RestSessionDetailJson> = gson.fromJson(entity.restSessions, type)
        return RestSession(
            id = entity.id,
            totalRestMinutes = entity.totalRestMinutes,
            restSessions = details.map { RestSessionDetail(it.durationMinutes, LocalTime.parse(it.time)) }
        )
    }

    fun toEntity(domain: RestSession): RestSessionEntity {
        val details = domain.restSessions.map { RestSessionDetailJson(it.durationMinutes, it.time.toString()) }
        return RestSessionEntity(
            id = domain.id,
            totalRestMinutes = domain.totalRestMinutes,
            restSessions = gson.toJson(details)
        )
    }

    private data class RestSessionDetailJson(
        val durationMinutes: Int,
        val time: String
    )
}

