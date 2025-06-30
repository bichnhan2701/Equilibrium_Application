package com.example.worklifebalance.ui.component.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.ui.theme.*
import com.example.worklifebalance.R
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.GoalType
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.model.autoGoalType

@Composable
fun GoalSummaryCard(
    goals: List<Goal>,
    tasks: List<Task>,
    executedDatesMap: Map<String, List<Long>>,
) {
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
            Text(
                text = "Tổng quan mục tiêu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val now = System.currentTimeMillis()
                val goalsWithType = goals.map { goal ->
                    val type = goal.autoGoalType(tasks, executedDatesMap, now)
                    goal to type
                }
                ProgressMetric(
                    value = "${goalsWithType.count { it.second == GoalType.COMPLETED.name }}",
                    label = "Mục tiêu\nhoàn thành",
                    icon = R.drawable.check_circle_svgrepo_com,
                    color = PastelGreen
                )

                ProgressMetric(
                    value = "${goalsWithType.count { it.second == GoalType.IN_PROGRESS.name }}",
                    label = "Mục tiêu\nđang tiến hành",
                    icon = R.drawable.time_sand_svgrepo_com,
                    color = PastelBlue
                )

                ProgressMetric(
                    value = "${goalsWithType.count { it.second == GoalType.NOT_STARTED.name }}",
                    label = "Mục tiêu\nchưa tiến hành",
                    icon = R.drawable.lock_folder_svgrepo_com,
                    color = PastelPurple
                )
            }
        }
    }
}


