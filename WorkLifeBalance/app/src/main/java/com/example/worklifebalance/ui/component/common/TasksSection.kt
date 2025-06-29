package com.example.worklifebalance.ui.component.common

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.model.TaskExecution

@Composable
fun TasksSection(
    titleTasksSection: String,
    tasks: List<Task>,
    domains: List<Domain>,
    goals: List<Goal>,
    executions: List<TaskExecution>,
    isTaskCompletedToday: (taskId: String) -> Boolean,
    onCheckTask: (task: Task) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onViewOrDeleteAll: () -> Unit,
    viewOrDeleteAllText: String,
) {
    var updateTask: Task? by remember { mutableStateOf(null) }
    var deleteTask: Task? by remember { mutableStateOf(null) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titleTasksSection,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { onViewOrDeleteAll() },
                ) {
                    Text(
                        text = viewOrDeleteAllText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (tasks.isEmpty()) {
                EmptyPlaceholder(
                    title = "Không có nhiệm vụ phù hợp"
                )
            } else {
                tasks.forEach { task ->
                    val domainName = domains.find { it.id == task.domainId }?.name ?: "Không xác định"
                    val isChecked = isTaskCompletedToday(task.id)
                    val executedDates = executions.filter { it.taskId == task.id }.map { it.executionDate }
                    // Chuẩn hóa plannedDates về 0h0p0s0ms
                    val normalizedPlannedDates = task.plannedDates.map {
                        val cal = java.util.Calendar.getInstance()
                        cal.timeInMillis = it
                        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                        cal.set(java.util.Calendar.MINUTE, 0)
                        cal.set(java.util.Calendar.SECOND, 0)
                        cal.set(java.util.Calendar.MILLISECOND, 0)
                        cal.timeInMillis
                    }
                    Log.d("TaskProgressDebug", "task: ${task.name}, plannedDates: ${normalizedPlannedDates}, executedDates: $executedDates, isChecked: $isChecked")

                    // Popup xác nhận hoàn thành
                    val showConfirmDialog = remember { mutableStateOf(false) }
                    if (showConfirmDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showConfirmDialog.value = false },
                            title = { Text("Xác nhận hoàn thành") },
                            text = { Text("Bạn đã chắc chắn hoàn thành nhiệm vụ này?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showConfirmDialog.value = false
                                    onCheckTask(task)
                                }) { Text("Xác nhận") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showConfirmDialog.value = false }) { Text("Huỷ") }
                            }
                        )
                    }

                    TaskItem(
                        task = task.copy(plannedDates = normalizedPlannedDates),
                        domainName = domainName,
                        isChecked = isChecked,
                        onCheckTask = {
                            if (!isChecked) {
                                showConfirmDialog.value = true
                            }
                        },
                        onUpdateTask = { updateTask = it },
                        onDeleteTask = { deleteTask = it },
                        goals = goals,
                        executedDates = executedDates
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
    if (updateTask != null) {
        EditTaskDialog(
            task = updateTask!!,
            domains = domains,
            goals = goals,
            onUpdate = { updatedTask ->
                onUpdateTask(updatedTask)
                updateTask = null
            },
            onDismiss = { updateTask = null }
        )
    }
    if (deleteTask != null) {
        AlertDialog(
            onDismissRequest = { deleteTask = null },
            title = { Text("Xác nhận xóa nhiệm vụ") },
            text = { Text("Bạn có chắc chắn muốn xóa nhiệm vụ này không?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteTask(deleteTask!!)
                    deleteTask = null
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = { deleteTask = null }) { Text("Hủy") }
            }
        )
    }
}