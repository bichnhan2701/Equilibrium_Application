package com.example.worklifebalance.domain.model

data class Goal(
    val id: String = "",
    val domainId: String = "",
    val name: String = "",
    val description: String? = null,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

fun Goal.progress(
    tasks: List<Task>,
    executedDatesMap: Map<String, List<Long>>
): Float {
    val relatedTasks = tasks.filter { it.goalId == this.id }
    if (relatedTasks.isEmpty()) return 0f
    val progresses = relatedTasks.map { task ->
        val executedDates = executedDatesMap[task.id] ?: emptyList()
        val weekDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.WEEKLY.name) task.plannedDates.map { it.toInt() }.toSet() else emptySet()
        val monthDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.MONTHLY.name) task.plannedDates.map { it.toInt() }.toSet() else emptySet()
        task.progress(
            executedDates = executedDates,
            goalStartDate = this.startDate,
            goalEndDate = this.endDate,
            weekDays = weekDays,
            monthDays = monthDays
        )
    }
    return progresses.average().toFloat()
}

fun Goal.expectedProgressForGoalToday(
    tasks: List<Task>,
    weekDaysMap: Map<String, Set<Int>> = emptyMap(),
    monthDaysMap: Map<String, Set<Int>> = emptyMap(),
    today: Long = System.currentTimeMillis()
): Float {
    val relatedTasks = tasks.filter { it.goalId == this.id }
    if (relatedTasks.isEmpty()) return 0f
    val progresses = relatedTasks.map { task ->
        val weekDays = weekDaysMap[task.id] ?: emptySet()
        val monthDays = monthDaysMap[task.id] ?: emptySet()
        task.expectedProgressForTaskToday(
            goalStartDate = this.startDate,
            goalEndDate = this.endDate,
            weekDays = weekDays,
            monthDays = monthDays,
            today = today
        )
    }
    return progresses.average().toFloat()
}

fun Goal.autoGoalType(
    tasks: List<Task>,
    executedDatesMap: Map<String, List<Long>>,
    now: Long = System.currentTimeMillis()
): String {
    val progress = this.progress(tasks, executedDatesMap)
    return when {
        now < this.startDate || progress == 0f -> GoalType.NOT_STARTED.name
        (now in this.startDate..this.endDate && progress > 0f && progress < 1f) -> GoalType.IN_PROGRESS.name
        now > this.endDate || progress >= 1f -> GoalType.COMPLETED.name
        else -> GoalType.NOT_STARTED.name
    }
}

fun countCompletedTasks(tasks: List<Task>, executedDatesMap: Map<String, List<Long>>, goals: List<Goal>): Int {
    return tasks.count { task ->
        val goal = goals.find { it.id == task.goalId }
        if (goal != null) {
            val executedDates = executedDatesMap[task.id] ?: emptyList()
            val weekDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.WEEKLY.name) task.plannedDates.map { it.toInt() }.toSet() else emptySet()
            val monthDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.MONTHLY.name) task.plannedDates.map { it.toInt() }.toSet() else emptySet()
            task.progress(
                executedDates = executedDates,
                goalStartDate = goal.startDate,
                goalEndDate = goal.endDate,
                weekDays = weekDays,
                monthDays = monthDays
            ) >= 1f
        } else false
    }
}