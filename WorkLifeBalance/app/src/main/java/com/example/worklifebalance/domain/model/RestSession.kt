package com.example.worklifebalance.domain.model

import java.time.LocalTime
import java.time.LocalDateTime

// Represents a single rest session
data class RestSessionDetail(
    val durationMinutes: Int, // e.g. 15
    val time: LocalTime, // e.g. 08:00
    val dateTime: LocalDateTime // full date and time of the rest session
)

// Represents the user's rest info
data class RestSession(
    val id: Long = 0,
    val totalRestMinutes: Int,
    val restSessions: List<RestSessionDetail>
)

data class RestEvent(
    val time: String,
    val activityName: String
)