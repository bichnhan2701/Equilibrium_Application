package com.example.worklifebalance.domain.model

enum class GoalType(val display: String) {
    NOT_STARTED("Chưa bắt đầu"),
    IN_PROGRESS("Đang thực hiện"),
    COMPLETED("Hoàn thành");

    companion object {
        fun fromString(value: String?): GoalType {
            if (value == null) return NOT_STARTED
            return GoalType.entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: NOT_STARTED
        }

        fun GoalType.toDisplay(): String = this.display
    }
}
