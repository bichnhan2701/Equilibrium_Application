package com.example.worklifebalance.ui.component.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.R
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.ui.theme.PastelGray
import com.example.worklifebalance.ui.theme.PastelTeal

@Composable
fun DomainSummaryCard(
    domains: List<Domain>,
    tasks: List<Task>
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
                text = "Tổng quan lĩnh vực và nhiệm vụ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressMetric(
                    value = "${domains.size}",
                    label = "Lĩnh vực\ntổng cộng",
                    icon = R.drawable.healthicons_life_science,
                    color = PastelTeal
                )

                ProgressMetric(
                    value = "${tasks.size }",
                    label = "Nhiệm vụ\ntổng cộng",
                    icon = R.drawable.task_list_svgrepo_com,
                    color = PastelGray
                )
            }
        }
    }
}