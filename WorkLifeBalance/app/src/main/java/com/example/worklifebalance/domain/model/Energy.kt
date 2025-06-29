package com.example.worklifebalance.domain.model

data class Energy(
    val id: String = "",
    val energy: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)

