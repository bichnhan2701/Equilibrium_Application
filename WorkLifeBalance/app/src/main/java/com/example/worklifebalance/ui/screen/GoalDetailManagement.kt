package com.example.worklifebalance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.common.TasksSection
import com.example.worklifebalance.ui.component.goal.GoalAdjustmentDialog
import com.example.worklifebalance.ui.component.goal.GoalInfo
import com.example.worklifebalance.ui.component.task.TaskFilterByDateBar
import com.example.worklifebalance.ui.viewmodel.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailManagement (
    navController: NavController,
    goalId: String,
    taskViewModel: TaskViewModel = hiltViewModel(),
    goalViewModel: GoalViewModel = hiltViewModel(),
    domainViewModel: DomainViewModel = hiltViewModel(),
    taskExecutionViewModel: TaskExecutionViewModel = hiltViewModel()
){
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val goal by goalViewModel.goalById.collectAsState()
    val domains by domainViewModel.domains.collectAsState()
    val goals by goalViewModel.goals.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()
    val executions by taskExecutionViewModel.executions.collectAsState()

    val domainColor = domains.find { it.id == goal?.domainId }?.color ?: 0xFF888888UL
    val domainName = domains.find { it.id == goal?.domainId }?.name ?: "Không xác định"
    val tasksOfGoal = tasks.filter { it.goalId == goalId }

    val today = LocalDate.now()
    val todayMillis = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
    val filteredTasks = tasks.filter { task ->
        task.plannedDates.any { dateMillis -> dateMillis == todayMillis }
    }
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun isTaskCompletedToday(taskId: String): Boolean {
        val today = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
        return executions.any { it.taskId == taskId && it.executionDate == today }
    }

    var showGoalAdjustmentDialog by remember { mutableStateOf(false) }
    var showConfirmDeletingAllTask by remember { mutableStateOf(false) }

    LaunchedEffect(goalId) {
        goalViewModel.getGoalById(goalId)
        goalViewModel.loadGoals()
        taskViewModel.loadTasks()
        taskViewModel.getTasksByGoalId(goalId)
        taskExecutionViewModel.loadAllExecutions()
        domainViewModel.loadDomains()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "Chi tiết mục tiêu",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                goal?.let {
                    val executedDatesMap = tasksOfGoal.associate { task ->
                        task.id to executions.filter { it.taskId == task.id }.map { it.executionDate }
                    }
                    GoalInfo(
                        goal = it,
                        domainName = domainName,
                        domainColor = domainColor,
                        onUpdateGoal = { goal ->
                            goalViewModel.updateGoal(goal)
                        },
                        onDeleteGoal = { goal ->
                            goalViewModel.deleteGoal(goal)
                        },
                        onViewSuggest = { showGoalAdjustmentDialog = true },
                        tasks = tasksOfGoal,
                        executedDatesMap = executedDatesMap
                    )
                }
            }
            item {
                TaskFilterByDateBar(
                    selectedDate = selectedDate?.format(dateFormatter) ?: "",
                    onDateSelected = { dateString ->
                        selectedDate = dateString.takeIf { it.isNotEmpty() }
                            ?.let { LocalDate.parse(it, dateFormatter) }
                    },
                    onClearDate = { selectedDate = null }
                )
            }
            item {
                TasksSection(
                    titleTasksSection = "Danh sách nhiệm vụ",
                    tasks = filteredTasks,
                    domains = domains,
                    goals = goals,
                    executions = executions,
                    isTaskCompletedToday = ::isTaskCompletedToday,
                    onCheckTask = { task ->
                        val execution = com.example.worklifebalance.domain.model.TaskExecution(
                            taskId = task.id,
                            executionDate = System.currentTimeMillis()
                        )
                        taskExecutionViewModel.insertTaskExecution(execution)
                    },
                    onUpdateTask = { task ->
                        taskViewModel.updateTask(task) },
                    onDeleteTask = { task ->
                        taskViewModel.deleteTask(task) },
                    onViewOrDeleteAll = { showConfirmDeletingAllTask = true },
                    viewOrDeleteAllText = "Xóa tất cả"
                )
            }
        }
    }
    if(showConfirmDeletingAllTask) {
        AlertDialog(
            onDismissRequest = { showConfirmDeletingAllTask = false },
            title = { Text("Xác nhận xóa nhiệm vụ") },
            text = { Text("Bạn có chắc chắn muốn xóa tất cả nhiệm vụ này không?") },
            confirmButton = {
                Button(onClick = {
                    taskViewModel.deleteAllTasks()
                    showConfirmDeletingAllTask = false
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeletingAllTask = false }) { Text("Hủy") }
            }
        )
    }
    // Goal Adjustment Dialog
    if (showGoalAdjustmentDialog) {
        GoalAdjustmentDialog(
            goal = goal,
            onDismiss = { showGoalAdjustmentDialog = false }
        )
    }
}