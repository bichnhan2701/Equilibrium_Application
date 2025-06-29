package com.example.worklifebalance.domain.model

enum class TaskDifficulty(val display: String) {
    LIGHT("Nhẹ nhàng"),
    FOCUS("Tập trung"),
    RELAX("Thư giãn");

    companion object {
        fun fromString(value: String?): TaskDifficulty {
            if (value == null) return LIGHT
            return TaskDifficulty.entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: LIGHT
        }

        fun TaskDifficulty.toDisplay(): String = this.display
    }
}