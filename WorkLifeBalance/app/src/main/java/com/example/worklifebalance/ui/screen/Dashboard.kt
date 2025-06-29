package com.example.worklifebalance.ui.screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.ui.theme.WorkLifeBalanceTheme
import com.example.worklifebalance.ui.viewmodel.EnergyViewModel
import com.example.worklifebalance.ui.viewmodel.TaskViewModel
import com.example.worklifebalance.ui.viewmodel.DomainViewModel
import com.example.worklifebalance.ui.viewmodel.GoalViewModel
import com.example.worklifebalance.ui.viewmodel.TaskExecutionViewModel
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.domain.utils.getGreetingByTime
import com.example.worklifebalance.domain.utils.getLastTwoWords
import com.example.worklifebalance.navigation.Screen
import com.example.worklifebalance.ui.component.dashboard.*
import com.example.worklifebalance.ui.component.common.TasksSection
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.ui.component.common.GoalsSection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import com.example.worklifebalance.R
import com.example.worklifebalance.ui.component.common.AddDomainDialog
import com.example.worklifebalance.ui.component.common.AddGoalDialog

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Dashboard(
    navController : NavController,
    energyViewModel: EnergyViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    domainViewModel: DomainViewModel = hiltViewModel(),
    goalViewModel: GoalViewModel = hiltViewModel(),
    taskExecutionViewModel: TaskExecutionViewModel = hiltViewModel()
) {
    val latestEnergy by energyViewModel.latestEnergy.collectAsState()
    val isLoading by energyViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("energy_prefs", Context.MODE_PRIVATE) }

    var showDialog by remember { mutableStateOf(false) }
    var inputEnergy by remember { mutableStateOf("") }
    var isInputError by remember { mutableStateOf(false) }
    var showAddNewPopup by remember { mutableStateOf(false) }
    var showEnergyChangePopup by remember { mutableStateOf(false) }
    var lastEnergyValue by remember { mutableStateOf(latestEnergy?.energy) }
    var showAddNewGoalPopup by remember { mutableStateOf(false) }
    var showAddNewDomainPopup by remember { mutableStateOf(false) }
    var showSuggestionPopup by remember { mutableStateOf(false) }

    val tasks by taskViewModel.tasks.collectAsState()
    val domains by domainViewModel.domains.collectAsState()
    val goals by goalViewModel.goals.collectAsState()
    val executions by taskExecutionViewModel.executions.collectAsState()

    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: ""

    // Theo dõi sự thay đổi của latestEnergy để hiển thị popup khi có cập nhật mới
    LaunchedEffect(latestEnergy) {
        val newValue = latestEnergy?.energy
        if (newValue != null && lastEnergyValue != null && newValue != lastEnergyValue) {
            showEnergyChangePopup = true
            // Nếu năng lượng mới <= 30 thì hiển thị SuggestionPopup
            if (newValue <= 30) {
                showSuggestionPopup = true
            }
            // Ẩn popup sau 2 giây
            kotlinx.coroutines.GlobalScope.launch {
                kotlinx.coroutines.delay(2000)
                showEnergyChangePopup = false
            }
        }
        lastEnergyValue = newValue
    }

    LaunchedEffect(Unit) {
        energyViewModel.loadAllEnergy()
        energyViewModel.loadLatestEnergy()
        taskViewModel.loadTasks()
        domainViewModel.loadDomains()
        goalViewModel.loadGoals()
        taskExecutionViewModel.loadAllExecutions()
    }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            inputEnergy = latestEnergy?.energy?.toString() ?: ""
        }
    }

    fun isTaskCompletedToday(taskId: String): Boolean {
        val today = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
        return executions.any { it.taskId == taskId && it.executionDate == today }
    }

    Scaffold (
        topBar = {
            DashboardHeader(
                onViewProfile = { navController.navigate(Screen.UserProfile.route) },
                onViewRest = { navController.navigate(Screen.Rest.route) },
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Lời chào và nút thêm mới
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${getGreetingByTime()} ${getLastTwoWords(userName)}!",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = getCurrentDate(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Button(
                        onClick = { showAddNewPopup = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm mới",
                            modifier = Modifier.size(26.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            // Thông tin nhanh về mục tiêu và nhiệm vụ
            item {
                QuickInfoCards(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    goalsCount = 0,
                    tasksCount = 0,
                    onViewTasks = {
                        navController.navigate(Screen.TaskManagement.route)
                    },
                    onViewGoals = {
                        navController.navigate(Screen.GoalManagement.route)
                    }
                )
            }
            // Năng lượng
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Luôn lấy latestEnergy mới nhất từ StateFlow, không phụ thuộc vào isLoading
                            val currentEnergy = latestEnergy
                            if (currentEnergy != null) {
                                val minutesAgo = (System.currentTimeMillis() - currentEnergy.updatedAt) / 60000
                                val timeText = when {
                                    minutesAgo < 1 -> "Vừa xong"
                                    minutesAgo < 60 -> "$minutesAgo phút trước"
                                    else -> "${minutesAgo / 60} giờ trước"
                                }
                                EnergyLevelCard(
                                    modifier = Modifier.weight(1f),
                                    energy = currentEnergy.energy,
                                    onEditEnergy = { showDialog = true },
                                    updateTime = timeText
                                )
                            } else {
                                EmptyCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Năng lượng",
                                    message = "Bạn chưa cập nhật năng lượng.",
                                    onEdit = { showDialog = true }
                                )
                            }
                            QuickInfoCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clickable {
                                        navController.navigate(Screen.DomainManagement.route)
                                    },
                                title = "Lĩnh vực",
                                value = domains.size.toString(),
                                icon = R.drawable.healthicons_life_science,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                iconBackground = MaterialTheme.colorScheme.primaryContainer,
                                description = "đang quan tâm"
                            )
                        }
                    }
                }
            }
            // Nhiệm vụ gợi ý
            item {
                TasksSection(
                    titleTasksSection = "Nhiệm vụ gợi ý",
                    tasks = tasks,
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
                        taskViewModel.updateTask(task)
                    },
                    onDeleteTask = { task ->
                        taskViewModel.deleteTask(task)
                    },
                    onViewOrDeleteAll = {
                        navController.navigate(Screen.TaskManagement.route)
                    },
                    viewOrDeleteAllText = "Xem tất cả"
                )
            }
            // Hiển thị danh sách muc tiêu
            item {
                // Chuẩn bị dữ liệu executedDatesMap cho GoalItem
                val executedDatesMap = tasks.associate { task ->
                    task.id to executions.filter { it.taskId == task.id }.map { it.executionDate }
                }
                GoalsSection(
                    goals = goals,
                    domains = domains,
                    onGoalClick = { goalId ->
                        navController.navigate(Screen.GoalDetailManagement.goalDetailManagementRoute(goalId))
                    },
                    onUpdateGoal = {
                        goalViewModel.updateGoal(it)
                        goalViewModel.loadGoals()
                    },
                    onDeleteGoal = {
                        goalViewModel.deleteGoal(it)
                        goalViewModel.loadGoals()
                    },
                    onViewOrDeleteAll = {
                        navController.navigate(Screen.GoalManagement.route)
                    },
                    viewOrDeleteAllText = "Xem tất cả",
                    tasks = tasks,
                    executedDatesMap = executedDatesMap
                )
            }
        }
    }
    // Energy Level Change Popup
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = showEnergyChangePopup && latestEnergy != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            EnergyChangePopup(energy = latestEnergy?.energy ?: 0)
        }
    }
    // Suggestion Popup
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = showSuggestionPopup,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(bottom = 16.dp, end = 16.dp),
        ) {
            SuggestionPopup(
                onDismiss = { showSuggestionPopup = false },
                onExploreActivityClick = {
                    showSuggestionPopup = false
                    navController.navigate(Screen.Rest.route)
                }
            )
        }
    }

    if (showDialog) {
        EnergyRecording(
            energy = inputEnergy.toIntOrNull() ?: (latestEnergy?.energy ?: 0),
            onDismissRequest = { showDialog = false },
            onConfirm = {
                val value = inputEnergy.toIntOrNull()
                if (value == null) {
                    isInputError = true
                } else {
                    val now = System.currentTimeMillis()
                    energyViewModel.viewModelScope.launch {
                        energyViewModel.insertEnergy(Energy(energy = value, updatedAt = now))
                        // Gọi lại loadAllEnergy để cập nhật latestEnergy từ database
                        energyViewModel.loadAllEnergy()
                        energyViewModel.loadLatestEnergy()
                        showDialog = false
                        // Lưu thời gian nhập năng lượng
                        prefs.edit { putLong("last_energy_time", now) }
                    }
                }
            },
            onEnergyChange = {
                inputEnergy = it.toString()
                isInputError = false
            }
        )
    }
    if (showAddNewPopup) {
        AddNewPopup(
            onDismiss = { showAddNewPopup = false },
            onAddDomain = {
                showAddNewPopup = false
                showAddNewDomainPopup = true
            },
            onAddGoal = {
                showAddNewPopup = false
                showAddNewGoalPopup = true
            },
            onAddTask = { showAddNewPopup = false  }
        )
    }
    if (showAddNewGoalPopup) {
        AddGoalDialog(
            context = context,
            domains = domains,
            onAdd = { goal ->
                goalViewModel.insertGoal(goal)
            },
            onDismiss = { showAddNewGoalPopup = false }
        )
    }
    if (showAddNewDomainPopup) {
        AddDomainDialog(
            onAdd = { domain ->
                domainViewModel.addDomain(domain)
                showAddNewDomainPopup = false
            },
            onDismiss = { showAddNewDomainPopup = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    WorkLifeBalanceTheme {
        Dashboard(
            navController = rememberNavController()
        )
    }
}