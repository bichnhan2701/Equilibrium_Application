package com.example.worklifebalance.domain.model

enum class EnergyLevel(val display: String) {
    HIGH("Cao"),
    MEDIUM("Trung bình"),
    LOW("Thấp");

    companion object {
        fun fromInt(value: Int): EnergyLevel = when {
            value >= 70 -> HIGH
            value >= 30 -> MEDIUM
            else -> LOW
        }
        fun fromString(value: String?): EnergyLevel {
            if (value == null) return MEDIUM
            return entries.find {
                it.name.equals(value, ignoreCase = true) || it.display.equals(value, ignoreCase = true)
            } ?: MEDIUM
        }
    }
}