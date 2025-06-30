package com.example.worklifebalance.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.worklifebalance.domain.model.*
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.common.AddTaskDialog
import com.example.worklifebalance.ui.component.common.EmptyPlaceholder
import com.example.worklifebalance.ui.component.common.TasksSection
import com.example.worklifebalance.ui.component.task.TaskFilterByDateBar
import com.example.worklifebalance.ui.component.task.TaskFilterByDomainBar
import com.example.worklifebalance.ui.viewmodel.DomainViewModel
import com.example.worklifebalance.ui.viewmodel.GoalViewModel
import com.example.worklifebalance.ui.viewmodel.TaskExecutionViewModel
import com.example.worklifebalance.ui.viewmodel.TaskViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat", "DefaultLocale")
@Composable
fun TaskManagement(
    taskViewModel: TaskViewModel = hiltViewModel(),
    goalViewModel: GoalViewModel = hiltViewModel(),
    domainViewModel: DomainViewModel = hiltViewModel(),
    taskExecutionViewModel: TaskExecutionViewModel = hiltViewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val goals by goalViewModel.goals.collectAsState()
    val domains by domainViewModel.domains.collectAsState()
    val executions by taskExecutionViewModel.executions.collectAsState()

    var selectedFilter by remember { mutableStateOf<Domain?>(null) }
    var selectedDate by remember { mutableStateOf("") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd-MM-yyyy") }
    val selectedLocalDate = if (selectedDate.isNotEmpty()) LocalDate.parse(selectedDate, dateFormatter) else null
    val goalsList = if (selectedFilter == null) goals else goals.filter { it.domainId == selectedFilter?.id }
    val dateFilteredGoals = goalsList.filter { goal ->
        if (selectedLocalDate == null) true
        else {
            val startDate = Instant.ofEpochMilli(goal.startDate).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = Instant.ofEpochMilli(goal.endDate).atZone(ZoneId.systemDefault()).toLocalDate()
            !selectedLocalDate.isBefore(startDate) && !selectedLocalDate.isAfter(endDate)
        }
    }
    var showAddNewTaskPopup by remember { mutableStateOf(false) }
    var showConfirmDeletingAllTask by remember { mutableStateOf(false) }
    var goalIdToDeleteTasks by remember { mutableStateOf<String?>(null) }

    fun isTaskCompletedToday(taskId: String): Boolean {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return executions.any { it.taskId == taskId && it.executionDate == today }
    }

    LaunchedEffect(Unit) {
        domainViewModel.loadDomains()
        goalViewModel.loadGoals()
        taskViewModel.loadTasks()
        taskExecutionViewModel.loadAllExecutions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quản lý nhiệm vụ",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = getCurrentDate(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { showAddNewTaskPopup = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm mới",
                            modifier = Modifier.size(26.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item{
                TaskFilterByDomainBar(
                    domains = domains,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item{
                TaskFilterByDateBar(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onClearDate = { selectedDate = "" }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (dateFilteredGoals.isEmpty()) {
                item{
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Danh sách nhiệm vụ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            EmptyPlaceholder(
                                title = "Chưa có dữ liệu nhiệm vụ.",
                                description = "Hãy bắt đầu bằng việc thêm một mà lĩnh vực bạn quan tâm.",
                            )
                        }
                    }
                }
            }
            items(dateFilteredGoals.size) { index ->
                val goal = dateFilteredGoals[index]
                val tasksOfGoal = tasks.filter { task ->
                    // Lọc theo goalId
                    task.goalId == goal.id &&
                        // Lọc theo ngày (nếu có)
                        (selectedLocalDate == null || task.plannedDates.any { execDate ->
                            val execLocalDate = Instant.ofEpochMilli(execDate).atZone(ZoneId.systemDefault()).toLocalDate()
                            execLocalDate == selectedLocalDate
                        }) &&
                        // Lọc theo domain (nếu có)
                        (selectedFilter == null || task.domainId == selectedFilter!!.id)
                }
                TasksSection(
                    modifier = Modifier.fillMaxWidth(),
                    titleTasksSection = "Mục tiêu: ${ goal.name }",
                    tasks = tasksOfGoal,
                    domains = domains,
                    goals = goals,
                    executions = executions,
                    isTaskCompletedToday = ::isTaskCompletedToday,
                    onCheckTask = { task ->
                        val execution = TaskExecution(
                            taskId = task.id,
                            executionDate = System.currentTimeMillis()
                        )
                        taskExecutionViewModel.insertTaskExecution(execution)
                    },
                    onUpdateTask = { task ->
                        taskViewModel.updateTask(task)
                    },
                    onDeleteTask = { task ->
                        taskViewModel.deleteTask(task)
                    },
                    onViewOrDeleteAll = {
                        goalIdToDeleteTasks = goal.id
                        showConfirmDeletingAllTask = true
                    },
                    viewOrDeleteAllText = "Xóa tất cả",
                )
                Divider(modifier = Modifier.padding(16.dp))
                if (showAddNewTaskPopup) {
                    AddTaskDialog(
                        domains = domains,
                        goals = goals,
                        onAdd = { task ->
                            taskViewModel.insertTask(task)
                        },
                        onDismiss = { showAddNewTaskPopup = false }
                    )
                }
            }
        }
    }
    if(showConfirmDeletingAllTask && goalIdToDeleteTasks != null) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDeletingAllTask = false
                goalIdToDeleteTasks = null
            },
            title = { Text("Xác nhận xóa nhiệm vụ") },
            text = { Text("Bạn có chắc chắn muốn xóa tất cả nhiệm vụ của mục tiêu này không?") },
            confirmButton = {
                Button(onClick = {
                    taskViewModel.deleteTasksByGoalId(goalIdToDeleteTasks!!)
                    showConfirmDeletingAllTask = false
                    goalIdToDeleteTasks = null
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmDeletingAllTask = false
                    goalIdToDeleteTasks = null
                }) { Text("Hủy") }
            }
        )
    }
}
