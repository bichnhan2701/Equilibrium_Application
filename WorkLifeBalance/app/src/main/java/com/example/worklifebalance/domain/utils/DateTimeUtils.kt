package com.example.worklifebalance.domain.utils

import android.annotation.SuppressLint
import android.util.Log
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.model.TaskRepeatRule
import com.example.worklifebalance.domain.model.TaskType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


fun getDayOfWeek(date: String): String {
    return when (date) {
        "MONDAY" -> "Thứ 2"
        "TUESDAY" -> "Thứ 3"
        "WEDNESDAY" -> "Thứ 4"
        "THURSDAY" -> "Thứ 5"
        "FRIDAY" -> "Thứ 6"
        "SATURDAY" -> "Thứ 7"
        "SUNDAY" -> "Chủ nhật"
        else -> ""
    }
}

fun getGreetingByTime(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..10 -> "Buổi sáng vui vẻ,"
        in 11..17 -> "Buổi chiều vui vẻ,"
        in 18..22 -> "Buổi tối vui vẻ,"
        else -> "Chào bạn!"
    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateOfWeek = LocalDate.now().dayOfWeek.name
    val dateOfWeekFormatter = getDayOfWeek(dateOfWeek)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0
    val year = calendar.get(Calendar.YEAR)
    val dateString = "$dateOfWeekFormatter, ngày $day tháng $month năm $year"

    return dateString
}

@SuppressLint("SimpleDateFormat")
fun getPlannedDateOfTaskDisplay(task: Task, goals: List<Goal>): String {
    val TAG = "PlannedDateDisplay"
    Log.d(TAG, "task.id=${task.id}, taskType=${task.taskType}, repeatRule=${task.repeatRule}, plannedDates=${task.plannedDates}, goalId=${task.goalId}")
    val result = when (TaskType.fromString(task.taskType)) {
        TaskType.NORMAL -> {
            if (task.plannedDates.isNotEmpty()) {
                val dateStr = SimpleDateFormat("dd/MM/yyyy").format(Date(task.plannedDates.first()))
                "Ngày thực hiện: $dateStr"
            } else "Chưa chọn ngày thực hiện"
        }
        TaskType.REPEAT -> {
            val goal = goals.find { it.id == task.goalId }
            if (goal == null || goal.startDate == 0L) {
                "Chưa chọn Goal hoặc Goal chưa có ngày bắt đầu"
            } else {
                val start = goal.startDate
                val end = if (goal.endDate > 0) goal.endDate else start + 365L * 24 * 60 * 60 * 1000 // 1 năm nếu chưa có endDate
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                when (TaskRepeatRule.fromString(task.repeatRule)) {
                    TaskRepeatRule.DAILY -> {
                        val startStr = sdf.format(Date(start))
                        val endStr = sdf.format(Date(end))
                        "Hàng ngày từ $startStr đến $endStr"
                    }
                    TaskRepeatRule.WEEKLY -> {
                        val weekDays = task.plannedDates.map {
                            val cal = Calendar.getInstance(); cal.timeInMillis = it
                            // 0: Thứ 2, ..., 6: Chủ nhật
                            (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
                        }.toSet()
                        val weekDayNames = weekDays.sorted().joinToString(", ") { idx ->
                            when (idx) {
                                0 -> "Thứ 2"
                                1 -> "Thứ 3"
                                2 -> "Thứ 4"
                                3 -> "Thứ 5"
                                4 -> "Thứ 6"
                                5 -> "Thứ 7"
                                6 -> "Chủ nhật"
                                else -> ""
                            }
                        }
                        val startStr = sdf.format(Date(start))
                        val endStr = sdf.format(Date(end))
                        "$weekDayNames hàng tuần từ $startStr đến $endStr"
                    }
                    TaskRepeatRule.MONTHLY -> {
                        val monthDays = task.plannedDates.map {
                            val cal = Calendar.getInstance(); cal.timeInMillis = it
                            cal.get(Calendar.DAY_OF_MONTH)
                        }.toSet()
                        val daysStr = monthDays.sorted().joinToString(", ") { it.toString() }
                        val startStr = sdf.format(Date(start))
                        val endStr = sdf.format(Date(end))
                        "Ngày $daysStr hàng tháng từ $startStr đến $endStr"
                    }
                    TaskRepeatRule.CUSTOM -> {
                        if (task.plannedDates.isEmpty()) "Chưa chọn ngày" else "Ngày custom: " + task.plannedDates.joinToString(", ") { sdf.format(Date(it)) }
                    }
                    else -> ""
                }
            }
        }
        else -> ""
    }
    Log.d(TAG, "result=$result")
    return result
}

