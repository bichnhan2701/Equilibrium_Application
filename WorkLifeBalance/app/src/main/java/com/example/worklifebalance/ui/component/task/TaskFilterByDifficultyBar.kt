package com.example.worklifebalance.ui.component.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.TaskDifficulty

@Composable
fun TaskFilterByDifficultyBar(
    selectedFilter: TaskDifficulty?,
    onFilterSelected: (TaskDifficulty?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Mức độ",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.2f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedFilter == null) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onFilterSelected(null) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tất cả",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedFilter == null) Color.White else Color.Gray,
                        fontWeight = if (selectedFilter == null) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            TaskDifficulty.entries.forEach { difficulty ->
                item(difficulty.name) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedFilter == difficulty) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { onFilterSelected(difficulty) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = difficulty.display,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedFilter == difficulty) Color.White else Color.Gray,
                            fontWeight = if (selectedFilter == difficulty) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

