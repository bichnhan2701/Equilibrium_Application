package com.example.worklifebalance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domain")
data class DomainEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val color: ULong = 0xFF000000UL,
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", 0xFF000000UL)
}
