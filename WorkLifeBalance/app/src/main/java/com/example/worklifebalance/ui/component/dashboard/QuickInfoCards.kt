package com.example.worklifebalance.ui.component.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.R

@Composable
fun QuickInfoCards(
    modifier: Modifier = Modifier,
    tasksCount: Int,
    goalsCount: Int,
    onViewTasks: () -> Unit = {},
    onViewGoals: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tasks Card
            QuickInfoCard(
                modifier = Modifier.weight(1f).clickable { onViewTasks() },
                title = "Nhiệm vụ",
                icon = R.drawable.task_list_svgrepo_com,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                iconBackground = MaterialTheme.colorScheme.primaryContainer,
                value = "$tasksCount",
                description = "hôm nay"
            )
            // Goals Card
            QuickInfoCard(
                modifier = Modifier.weight(1f).clickable { onViewGoals() },
                title = "Mục tiêu",
                icon = R.drawable.octicon_goal_16,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                iconBackground = MaterialTheme.colorScheme.secondaryContainer,
                value = "$goalsCount",
                description = "đang tiến hành"
            )
        }
    }
}