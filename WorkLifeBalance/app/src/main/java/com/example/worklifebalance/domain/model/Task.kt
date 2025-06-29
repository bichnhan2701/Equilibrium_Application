package com.example.worklifebalance.domain.model

data class Task(
    val id: String = "",
    val domainId: String = "",
    val goalId: String = "",
    val name: String = "",
    val description: String? = null,
    val difficulty: String = "",
    val taskType: String = "",
    val plannedDates: List<Long> = emptyList(), // Danh sách ngày dự kiến thực hiện
    val plannedTime: String = "", // Thời gian dự kiến thực hiện nhiệm vụ
    val repeatRule: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

fun Task.totalPlannedDates(goalStartDate: Long, goalEndDate: Long, weekDays: Set<Int> = emptySet(), monthDays: Set<Int> = emptySet()): Int {
    return when (TaskType.fromString(taskType)) {
        TaskType.NORMAL -> if (plannedDates.isNotEmpty()) 1 else 0
        TaskType.REPEAT -> {
            val start = goalStartDate
            val end = if (goalEndDate > 0) goalEndDate else start + 365L * 24 * 60 * 60 * 1000 // 1 năm nếu chưa có endDate
            when (TaskRepeatRule.fromString(repeatRule)) {
                TaskRepeatRule.DAILY -> {
                    if (start > 0 && end >= start) ((end - start) / (24 * 60 * 60 * 1000) + 1).toInt() else 0
                }
                TaskRepeatRule.WEEKLY -> {
                    var count = 0
                    var d = start
                    val cal = java.util.Calendar.getInstance()
                    while (d <= end) {
                        cal.timeInMillis = d
                        val dayOfWeek = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7 // 0:Mon ... 6:Sun
                        if (weekDays.contains(dayOfWeek)) count++
                        d += 24 * 60 * 60 * 1000
                    }
                    count
                }
                TaskRepeatRule.MONTHLY -> {
                    var count = 0
                    var d = start
                    val cal = java.util.Calendar.getInstance()
                    while (d <= end) {
                        cal.timeInMillis = d
                        val dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH)
                        if (monthDays.contains(dayOfMonth)) count++
                        d += 24 * 60 * 60 * 1000
                    }
                    count
                }
                TaskRepeatRule.CUSTOM -> plannedDates.size
                else -> 0
            }
        }
        else -> 0
    }
}

fun Task.progress(executedDates: List<Long>, goalStartDate: Long, goalEndDate: Long, weekDays: Set<Int> = emptySet(), monthDays: Set<Int> = emptySet()): Float {
    val total = totalPlannedDates(goalStartDate, goalEndDate, weekDays, monthDays)
    if (total == 0) return 0f
    // Đếm số ngày planned đã được đánh dấu hoàn thành (có trong executedDates, so sánh theo ngày)
    val plannedDatesSet = when (TaskType.fromString(taskType)) {
        TaskType.NORMAL, TaskType.REPEAT -> {
            when (TaskRepeatRule.fromString(repeatRule)) {
                TaskRepeatRule.DAILY, TaskRepeatRule.WEEKLY, TaskRepeatRule.MONTHLY -> {
                    // Tính lại danh sách plannedDates dựa vào rule
                    val start = goalStartDate
                    val end = if (goalEndDate > 0) goalEndDate else start + 365L * 24 * 60 * 60 * 1000
                    val result = mutableSetOf<Long>()
                    val cal = java.util.Calendar.getInstance()
                    var d = start
                    while (d <= end) {
                        cal.timeInMillis = d
                        val dayOfWeek = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7
                        val dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH)
                        val match = when (TaskRepeatRule.fromString(repeatRule)) {
                            TaskRepeatRule.DAILY -> true
                            TaskRepeatRule.WEEKLY -> weekDays.contains(dayOfWeek)
                            TaskRepeatRule.MONTHLY -> monthDays.contains(dayOfMonth)
                            else -> false
                        }
                        if (match) result.add(d)
                        d += 24 * 60 * 60 * 1000
                    }
                    result
                }
                TaskRepeatRule.CUSTOM -> plannedDates.toSet()
                else -> plannedDates.toSet()
            }
        }
        else -> plannedDates.toSet()
    }
    // Chuẩn hóa executedDates về 0h để so sánh
    val executedSet = executedDates.map {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = it
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }.toSet()
    val completed = plannedDatesSet.count { executedSet.contains(it) }
    return completed.toFloat() / total
}

fun Task.expectedProgressForTaskToday(
    goalStartDate: Long,
    goalEndDate: Long,
    weekDays: Set<Int> = emptySet(),
    monthDays: Set<Int> = emptySet(),
    today: Long = System.currentTimeMillis()
): Float {
    val total = totalPlannedDates(goalStartDate, goalEndDate, weekDays, monthDays)
    if (total == 0) return 0f
    val plannedDatesSet = when (TaskType.fromString(taskType)) {
        TaskType.NORMAL, TaskType.REPEAT -> {
            when (TaskRepeatRule.fromString(repeatRule)) {
                TaskRepeatRule.DAILY, TaskRepeatRule.WEEKLY, TaskRepeatRule.MONTHLY -> {
                    val start = goalStartDate
                    val end = if (goalEndDate > 0) goalEndDate else start + 365L * 24 * 60 * 60 * 1000
                    val result = mutableSetOf<Long>()
                    val cal = java.util.Calendar.getInstance()
                    var d = start
                    while (d <= end) {
                        cal.timeInMillis = d
                        val dayOfWeek = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7
                        val dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH)
                        val match = when (TaskRepeatRule.fromString(repeatRule)) {
                            TaskRepeatRule.DAILY -> true
                            TaskRepeatRule.WEEKLY -> weekDays.contains(dayOfWeek)
                            TaskRepeatRule.MONTHLY -> monthDays.contains(dayOfMonth)
                            else -> false
                        }
                        if (match) result.add(d)
                        d += 24 * 60 * 60 * 1000
                    }
                    result
                }
                TaskRepeatRule.CUSTOM -> plannedDates.toSet()
                else -> plannedDates.toSet()
            }
        }
        else -> plannedDates.toSet()
    }
    val cal = java.util.Calendar.getInstance()
    cal.timeInMillis = today
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    val todayZero = cal.timeInMillis
    val dueCount = plannedDatesSet.count { it <= todayZero }
    return dueCount.toFloat() / total
}
