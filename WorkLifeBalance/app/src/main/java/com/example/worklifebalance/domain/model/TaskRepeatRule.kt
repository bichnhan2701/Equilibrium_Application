package com.example.worklifebalance.domain.model

enum class TaskRepeatRule( val display: String) {
    NONE("Không lặp lại"),
    DAILY("Hàng ngày"),
    WEEKLY("Hàng tuần"),
    MONTHLY("Hàng tháng"),
    CUSTOM("Tuỳ chỉnh");

    companion object {
        fun fromString(value: String?): TaskRepeatRule {
            if (value == null) return DAILY
            return TaskRepeatRule.entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: DAILY
        }

        fun TaskRepeatRule.toDisplay(): String = this.display
    }
}