package com.example.worklifebalance.ui.component.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.R
import com.example.worklifebalance.domain.model.*
import com.example.worklifebalance.domain.model.TaskType.Companion.fromString
import com.example.worklifebalance.domain.model.TaskType.Companion.toDisplay
import com.example.worklifebalance.domain.utils.getPlannedDateOfTaskDisplay
import com.example.worklifebalance.ui.theme.*
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun TaskItem(
    task: Task,
    domainName: String,
    isChecked: Boolean,
    onCheckTask: (() -> Unit)? = null,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    goals: List<Goal> = emptyList(),
    executedDates: List<Long> = emptyList()
) {
    val (backgroundColor, iconColor) = when (task.difficulty) {
        TaskDifficulty.LIGHT.name -> LightGreen to Green
        TaskDifficulty.FOCUS.name -> LightBlue to Blue
        TaskDifficulty.RELAX.name -> LightPurple to Purple
        else -> LightGray to Gray
    }

    val goal = goals.find { it.id == task.goalId }
    val goalStartDate = goal?.startDate ?: 0L
    val goalEndDate = goal?.endDate ?: 0L
    val weekDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.WEEKLY.name) task.plannedDates.map {
        val cal = Calendar.getInstance()
        cal.timeInMillis = it
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.get(Calendar.DAY_OF_WEEK).let { dow -> (dow + 5) % 7 }
    }.toSet() else emptySet()
    val monthDays = if (task.taskType == TaskType.REPEAT.name && task.repeatRule == TaskRepeatRule.MONTHLY.name) task.plannedDates.map {
        val cal = Calendar.getInstance()
        cal.timeInMillis = it
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.get(Calendar.DAY_OF_MONTH)
    }.toSet() else emptySet()
    val progress = task.progress(
        executedDates = executedDates,
        goalStartDate = goalStartDate,
        goalEndDate = goalEndDate,
        weekDays = weekDays,
        monthDays = monthDays
    )
    android.util.Log.d("TaskProgressDebug", "[TaskItem] task: ${task.name}, progress: $progress, executedDates: $executedDates, plannedDates: ${task.plannedDates}, weekDays: $weekDays, monthDays: $monthDays")
    val progressPercent = (progress * 100).roundToInt()
    val expectedProgress = task.expectedProgressForTaskToday(
        goalStartDate = goalStartDate,
        goalEndDate = goalEndDate,
        weekDays = weekDays,
        monthDays = monthDays
    )
    val isLate = progress + 0.001f < expectedProgress // allow for float rounding

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (onCheckTask != null && checked && !isChecked) {
                                onCheckTask()
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = iconColor,
                            uncheckedColor = Color.LightGray
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (task.description?.isNotEmpty() == true) {
                        Text(
                            text = task.description.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Task Type Tag or Late Tag
                        Surface(
                            color = if (isLate) Color(0xFFFFE0E0) else backgroundColor,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(
                                text = if (isLate) "Chậm tiến độ" else fromString(task.taskType).toDisplay(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isLate) Color.Red else iconColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Domain Tag
                        Surface(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(
                                text = domainName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
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
                                onUpdateTask(task)
                                expandedMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa") },
                            onClick = {
                                onDeleteTask(task)
                                expandedMenu.value = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Progress bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = iconColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Text("Tiến độ: $progressPercent%", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            // Planned date info
            val plannedDateDisplay = getPlannedDateOfTaskDisplay(task, goals)
            Text(plannedDateDisplay, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Energy Required
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Duration
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.time_svgrepo_com),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.plannedTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
