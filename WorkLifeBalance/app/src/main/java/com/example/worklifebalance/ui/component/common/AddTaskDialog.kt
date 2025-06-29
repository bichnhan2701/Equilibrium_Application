package com.example.worklifebalance.ui.component.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTaskDialog(
    domains: List<Domain>,
    goals: List<Goal>,
    onAdd: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedDomainId by remember { mutableStateOf("") }
    var selectedGoalId by remember { mutableStateOf("") }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf(TaskDifficulty.LIGHT.name) }
    var selectedTaskType by remember { mutableStateOf(TaskType.NORMAL.name) }
    var selectedRepeatRule by remember { mutableStateOf(TaskRepeatRule.NONE.name) }
    var plannedDates by remember { mutableStateOf(listOf<Long>()) }
    var plannedTime by remember { mutableStateOf("") }
    var selectedWeekDays by remember { mutableStateOf(setOf<Int>()) }
    var selectedMonthDays by remember { mutableStateOf(setOf<Int>()) }
    var customDates by remember { mutableStateOf(listOf<Long>()) }
    var plannedHour by remember { mutableIntStateOf(-1) }
    var plannedMinute by remember { mutableIntStateOf(-1) }

    val selectedGoal = goals.find { it.id == selectedGoalId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Thêm nhiệm vụ mới", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Column {
                // Domain selection
                var expandedDomain by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expandedDomain = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(domains.find { it.id == selectedDomainId }?.name ?: "Chọn lĩnh vực")
                    }
                    DropdownMenu(
                        expanded = expandedDomain,
                        onDismissRequest = { expandedDomain = false }
                    ) {
                        domains.forEach { domain ->
                            DropdownMenuItem(text = { Text(domain.name) }, onClick = {
                                selectedDomainId = domain.id
                                selectedGoalId = ""
                                expandedDomain = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Goal selection
                if (selectedDomainId.isNotBlank()) {
                    val filteredGoals = goals.filter { it.domainId == selectedDomainId }
                    var expandedGoal by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(onClick = { expandedGoal = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(filteredGoals.find { it.id == selectedGoalId }?.name ?: "Chọn mục tiêu")
                        }
                        DropdownMenu(
                            expanded = expandedGoal,
                            onDismissRequest = { expandedGoal = false }
                        ) {
                            filteredGoals.forEach { goal ->
                                DropdownMenuItem(text = { Text(goal.name) }, onClick = {
                                    selectedGoalId = goal.id
                                    expandedGoal = false
                                })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                OutlinedTextField(
                    value = newTaskName,
                    onValueChange = { newTaskName = it },
                    label = { Text("Tên Task") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newTaskDescription,
                    onValueChange = { newTaskDescription = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = false,
                    maxLines = 3
                )
                Spacer(Modifier.height(8.dp))
                // Difficulty
                var expandedDifficulty by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expandedDifficulty = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(TaskDifficulty.fromString(selectedDifficulty).display)
                    }
                    DropdownMenu(
                        expanded = expandedDifficulty,
                        onDismissRequest = { expandedDifficulty = false }
                    ) {
                        TaskDifficulty.entries.forEach { diff ->
                            DropdownMenuItem(text = { Text(diff.display) }, onClick = {
                                selectedDifficulty = diff.name
                                expandedDifficulty = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Task Type
                var expandedType by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expandedType = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(TaskType.fromString(selectedTaskType).display)
                    }
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        TaskType.entries.forEach { type ->
                            DropdownMenuItem(text = { Text(type.display) }, onClick = {
                                selectedTaskType = type.name
                                expandedType = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Repeat Rule
                if (TaskType.fromString(selectedTaskType) == TaskType.REPEAT) {
                    var expandedRule by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(onClick = { expandedRule = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(TaskRepeatRule.fromString(selectedRepeatRule).display)
                        }
                        DropdownMenu(
                            expanded = expandedRule,
                            onDismissRequest = { expandedRule = false }
                        ) {
                            TaskRepeatRule.entries.forEach { rule ->
                                DropdownMenuItem(text = { Text(rule.display) }, onClick = {
                                    selectedRepeatRule = rule.name
                                    expandedRule = false
                                })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (TaskRepeatRule.fromString(selectedRepeatRule) == TaskRepeatRule.WEEKLY) {
                        val weekDays = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                        LazyRow {
                            items(weekDays.indices.toList()) { idx ->
                                val selected = selectedWeekDays.contains(idx)
                                Button(
                                    onClick = {
                                        selectedWeekDays = if (selected) selectedWeekDays - idx else selectedWeekDays + idx
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected) Color.Blue else Color.LightGray
                                    ),
                                    modifier = Modifier.padding(end = 4.dp)
                                ) { Text(weekDays[idx]) }
                            }
                        }
                    }
                    if (TaskRepeatRule.fromString(selectedRepeatRule) == TaskRepeatRule.MONTHLY) {
                        LazyRow {
                            items((1..31).toList()) { day ->
                                val selected = selectedMonthDays.contains(day)
                                Button(
                                    onClick = {
                                        selectedMonthDays = if (selected) selectedMonthDays - day else selectedMonthDays + day
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected) Color.Blue else Color.LightGray
                                    ),
                                    modifier = Modifier.padding(end = 2.dp)
                                ) { Text(day.toString()) }
                            }
                        }
                    }
                    if (TaskRepeatRule.fromString(selectedRepeatRule) == TaskRepeatRule.CUSTOM) {
                        Button(onClick = {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    cal.set(year, month, dayOfMonth, 0, 0, 0)
                                    val millis = cal.timeInMillis
                                    if (!customDates.contains(millis)) customDates = customDates + millis
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) { Text("Chọn ngày") }
                        LazyRow {
                            items(customDates) { date ->
                                val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(date))
                                Box(
                                    modifier = Modifier.background(Color.LightGray, MaterialTheme.shapes.small).padding(8.dp)
                                ) { Text(dateStr) }
                            }
                        }
                    }
                }
                if (TaskType.fromString(selectedTaskType) == TaskType.NORMAL) {
                    val plannedDateText = if (plannedDates.isNotEmpty()) SimpleDateFormat("dd/MM/yyyy").format(Date(plannedDates.first())) else "Chọn ngày thực hiện"
                    Button(onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                cal.set(year, month, dayOfMonth, 0, 0, 0)
                                val millis = cal.timeInMillis
                                if (selectedGoal == null || (selectedGoal.startDate <= millis && (selectedGoal.endDate == 0L || millis <= selectedGoal.endDate))) {
                                    plannedDates = listOf(millis)
                                }
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }, modifier = Modifier.fillMaxWidth()) { Text(plannedDateText) }
                    Spacer(Modifier.height(8.dp))
                }
                // Planned Time
                val plannedTimeText = if (plannedHour >= 0 && plannedMinute >= 0) String.format("%02d:%02d", plannedHour, plannedMinute) else "Chọn giờ"
                Button(onClick = {
                    val cal = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            plannedHour = hourOfDay
                            plannedMinute = minute
                            plannedTime = String.format("%02d:%02d", hourOfDay, minute)
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                }, modifier = Modifier.fillMaxWidth()) { Text(plannedTimeText) }
            }
        },
        confirmButton = {
            Button(onClick = {
                val isRepeat = TaskType.fromString(selectedTaskType) == TaskType.REPEAT
                val repeatRule = TaskRepeatRule.fromString(selectedRepeatRule)
                val goalStart = selectedGoal?.startDate ?: 0L
                val goalEnd = if ((selectedGoal?.endDate ?: 0L) > 0) selectedGoal?.endDate ?: 0L else goalStart + 365L * 24 * 60 * 60 * 1000
                val plannedDatesCalculated = when (repeatRule) {
                    TaskRepeatRule.WEEKLY -> {
                        val weekDays = selectedWeekDays
                        val result = mutableListOf<Long>()
                        var d = goalStart
                        val cal = Calendar.getInstance()
                        while (d <= goalEnd) {
                            cal.timeInMillis = d
                            val dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
                            if (weekDays.contains(dayOfWeek)) {
                                result.add(d)
                            }
                            d += 24 * 60 * 60 * 1000
                        }
                        result
                    }
                    TaskRepeatRule.MONTHLY -> {
                        val monthDays = selectedMonthDays
                        val result = mutableListOf<Long>()
                        var d = goalStart
                        val cal = Calendar.getInstance()
                        while (d <= goalEnd) {
                            cal.timeInMillis = d
                            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
                            if (monthDays.contains(dayOfMonth)) {
                                result.add(d)
                            }
                            d += 24 * 60 * 60 * 1000
                        }
                        result
                    }
                    TaskRepeatRule.DAILY -> {
                        val result = mutableListOf<Long>()
                        var d = goalStart
                        while (d <= goalEnd) {
                            result.add(d)
                            d += 24 * 60 * 60 * 1000
                        }
                        result
                    }
                    TaskRepeatRule.CUSTOM -> customDates
                    else -> plannedDates
                }
                val allDatesValid = plannedDatesCalculated.all { millis ->
                    selectedGoal == null || (selectedGoal.startDate <= millis && (selectedGoal.endDate == 0L || millis <= selectedGoal.endDate))
                }
                if (!allDatesValid) return@Button
                if (newTaskName.isNotBlank() && selectedGoalId.isNotBlank() && selectedDomainId.isNotBlank()) {
                    val task = Task(
                        id = System.currentTimeMillis().toString(),
                        domainId = selectedDomainId,
                        goalId = selectedGoalId,
                        name = newTaskName,
                        description = newTaskDescription,
                        difficulty = selectedDifficulty,
                        taskType = selectedTaskType,
                        plannedDates = plannedDatesCalculated,
                        plannedTime = plannedTime,
                        repeatRule = selectedRepeatRule
                    )
                    onAdd(task)
                    onDismiss()
                }
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}