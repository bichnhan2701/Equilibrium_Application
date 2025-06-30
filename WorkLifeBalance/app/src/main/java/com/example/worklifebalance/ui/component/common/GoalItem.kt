package com.example.worklifebalance.ui.component.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.model.expectedProgressForGoalToday
import com.example.worklifebalance.domain.model.progress
import com.example.worklifebalance.ui.theme.LightRed
import com.example.worklifebalance.ui.theme.Red
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(millis: Long): String {
    if (millis == 0L) return "Không xác định"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}

@Composable
fun GoalItem(
    goal: Goal,
    domainName: String,
    domainColor: ULong,
    onViewDetail: (goalId: String) -> Unit,
    onUpdateGoal: (Goal) -> Unit,
    onDeleteGoal: (Goal) -> Unit,
    tasks: List<Task>,
    executedDatesMap: Map<String, List<Long>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(domainColor).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = domainName,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(domainColor),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if( goal.description.isNullOrEmpty().not() ) {
                        Text(
                            text = goal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text = "Hạn: ${formatDate(goal.endDate)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                var expandedMenu = remember { mutableStateOf(false) }
                IconButton(onClick = { expandedMenu.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Thêm tùy chọn",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DropdownMenu(
                        expanded = expandedMenu.value,
                        onDismissRequest = { expandedMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sửa") },
                            onClick = {
                                expandedMenu.value = false
                                onUpdateGoal(goal)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa") },
                            onClick = {
                                expandedMenu.value = false
                                onDeleteGoal(goal)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { goal.progress(tasks, executedDatesMap) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(domainColor),
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                val percent = (goal.progress(tasks, executedDatesMap) * 100).toInt()
                Text(
                    text = "$percent% hoàn thành",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onViewDetail(goal.id) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(domainColor)
                    )
                ) {
                    Text("Xem chi tiết")
                }
                val actualProgress = goal.progress(tasks, executedDatesMap)
                val expectedProgress = goal.expectedProgressForGoalToday(tasks)
                val isLate = actualProgress < expectedProgress && actualProgress < 1f
                if (isLate) {
                    Surface(
                        color = LightRed,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Chậm tiến độ",
                            style = MaterialTheme.typography.labelSmall,
                            color = Red,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}