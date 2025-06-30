package com.example.worklifebalance.ui.component.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.Task

@Composable
fun GoalsSection(
    titleDisplay: String,
    goals: List<Goal>,
    domains: List<Domain>,
    onGoalClick: (goalId: String) -> Unit,
    onUpdateGoal: (Goal) -> Unit,
    onDeleteGoal: (Goal) -> Unit,
    onViewOrDeleteAll: () -> Unit,
    viewOrDeleteAllText: String,
    tasks: List<Task>,
    executedDatesMap: Map<String, List<Long>>
) {
    val context = LocalContext.current
    var editingGoal by remember { mutableStateOf<Goal?>(null) }
    var deletingGoal by remember { mutableStateOf<Goal?>(null) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titleDisplay,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = { onViewOrDeleteAll() }) {
                    Text(
                        text = viewOrDeleteAllText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (goals.isEmpty()) {
                EmptyPlaceholder(
                    title = "Không có mục tiêu nào phù hợp",
                    description = "",
                )
            } else {
                goals.forEach { goal ->
                    val domain = domains.find { it.id == goal.domainId }
                    val domainName = domain?.name ?: "Khác"
                    val domainColor = domain?.color?.toULong() ?: 0xFF888888UL
                    GoalItem(
                        goal = goal,
                        domainName = domainName,
                        domainColor = domainColor,
                        onViewDetail = { onGoalClick(goal.id) },
                        onUpdateGoal = { editingGoal = goal },
                        onDeleteGoal = { deletingGoal = goal },
                        tasks = tasks,
                        executedDatesMap = executedDatesMap
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
    if (editingGoal != null) {
        EditGoalDialog(
            context = context,
            domains = domains,
            goal = editingGoal!!,
            onUpdate = { updatedGoal ->
                onUpdateGoal(updatedGoal)
                editingGoal = null
            },
            onDismiss = { editingGoal = null }
        )
    }
    if (deletingGoal != null) {
        AlertDialog(
            onDismissRequest = { deletingGoal = null },
            title = { Text("Xác nhận xóa mục tiêu") },
            text = { Text("Bạn có chắc chắn muốn xóa mục tiêu này không?") },
            confirmButton = {
                Button(onClick = {
                    onDeleteGoal(deletingGoal!!)
                    deletingGoal = null
                    editingGoal = null
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = { deletingGoal = null }) { Text("Hủy") }
            }
        )
    }
}