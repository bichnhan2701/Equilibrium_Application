package com.example.worklifebalance.domain.model

enum class TaskType(val display: String) {
    REPEAT("Lặp lại"),
    NORMAL("Bình thường");

    companion object {
        fun fromString(value: String?): TaskType {
            if (value == null) return NORMAL
            return TaskType.entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: NORMAL
        }

        fun TaskType.toDisplay(): String = this.display
    }
}