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
            restSessions = details.map {
                val parsedTime = LocalTime.parse(it.time)
                val now = java.time.LocalDateTime.now()
                val todayDate = now.toLocalDate()
                val fallbackDateTime =
                    if (parsedTime.isBefore(now.toLocalTime()) || parsedTime == now.toLocalTime())
                        java.time.LocalDateTime.of(todayDate, parsedTime)
                    else
                        java.time.LocalDateTime.of(todayDate.minusDays(1), parsedTime)
                val parsedDateTime = try {
                    if (!it.dateTime.isNullOrBlank()) java.time.LocalDateTime.parse(it.dateTime)
                    else fallbackDateTime
                } catch (e: Exception) {
                    fallbackDateTime
                }
                RestSessionDetail(
                    it.durationMinutes,
                    parsedTime,
                    parsedDateTime
                )
            }
        )
    }

    fun toEntity(domain: RestSession): RestSessionEntity {
        val details = domain.restSessions.map { RestSessionDetailJson(
            it.durationMinutes,
            it.time.toString(),
            it.dateTime.toString()
        ) }
        return RestSessionEntity(
            id = domain.id,
            totalRestMinutes = domain.totalRestMinutes,
            restSessions = gson.toJson(details)
        )
    }

    private data class RestSessionDetailJson(
        val durationMinutes: Int,
        val time: String,
        val dateTime: String
    )
}
