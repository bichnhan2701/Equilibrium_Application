package com.example.worklifebalance.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.ui.theme.WorkLifeBalanceTheme
import com.example.worklifebalance.ui.viewmodel.*
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.worklifebalance.domain.utils.*
import com.example.worklifebalance.navigation.Screen
import com.example.worklifebalance.ui.component.dashboard.*
import com.example.worklifebalance.ui.component.common.TasksSection
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.ui.component.common.GoalsSection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import com.example.worklifebalance.R
import com.example.worklifebalance.domain.model.TaskDifficulty
import com.example.worklifebalance.domain.model.autoGoalType
import com.example.worklifebalance.domain.model.progress
import com.example.worklifebalance.ui.component.common.AddDomainDialog
import com.example.worklifebalance.ui.component.common.AddTaskDialog
import com.example.worklifebalance.ui.component.common.AddGoalDialog
import com.example.worklifebalance.ui.component.common.EmptyPlaceholder
import com.example.worklifebalance.ui.component.restsuggestion.ReminderToast
import com.example.worklifebalance.ui.component.task.TaskFilterByDifficultyBar
import kotlinx.coroutines.delay

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
    var showAddNewTaskPopup by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showReminderToast by remember { mutableStateOf(false) }
    var reminderTimesReloadKey by remember { mutableIntStateOf(0) }

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
                delay(5000)
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

    // Lấy danh sách nhiệm vụ hôm nay
    val today = remember {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }
    val todayTasks = remember(tasks, goals) {
        tasks.filter { task ->
            when (com.example.worklifebalance.domain.model.TaskType.fromString(task.taskType)) {
                com.example.worklifebalance.domain.model.TaskType.NORMAL -> {
                    task.plannedDates.any {
                        val cal = java.util.Calendar.getInstance()
                        cal.timeInMillis = it
                        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                        cal.set(java.util.Calendar.MINUTE, 0)
                        cal.set(java.util.Calendar.SECOND, 0)
                        cal.set(java.util.Calendar.MILLISECOND, 0)
                        cal.timeInMillis == today
                    }
                }
                com.example.worklifebalance.domain.model.TaskType.REPEAT -> {
                    val repeatRule = com.example.worklifebalance.domain.model.TaskRepeatRule.fromString(task.repeatRule)
                    when (repeatRule) {
                        com.example.worklifebalance.domain.model.TaskRepeatRule.DAILY -> {
                            // Nếu plannedDates có ngày hôm nay thì hiển thị
                            task.plannedDates.any {
                                val cal = java.util.Calendar.getInstance()
                                cal.timeInMillis = it
                                cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                                cal.set(java.util.Calendar.MINUTE, 0)
                                cal.set(java.util.Calendar.SECOND, 0)
                                cal.set(java.util.Calendar.MILLISECOND, 0)
                                cal.timeInMillis == today
                            }
                        }
                        com.example.worklifebalance.domain.model.TaskRepeatRule.WEEKLY -> {
                            // plannedDates chứa các ngày lặp lại (dạng millis), kiểm tra nếu có ngày nào là cùng thứ với hôm nay và trùng ngày
                            task.plannedDates.any {
                                val plannedCal = java.util.Calendar.getInstance()
                                plannedCal.timeInMillis = it
                                plannedCal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                                plannedCal.set(java.util.Calendar.MINUTE, 0)
                                plannedCal.set(java.util.Calendar.SECOND, 0)
                                plannedCal.set(java.util.Calendar.MILLISECOND, 0)
                                val todayCal = java.util.Calendar.getInstance()
                                todayCal.timeInMillis = today
                                plannedCal.get(java.util.Calendar.DAY_OF_WEEK) == todayCal.get(java.util.Calendar.DAY_OF_WEEK) &&
                                plannedCal.timeInMillis == todayCal.timeInMillis
                            }
                        }
                        com.example.worklifebalance.domain.model.TaskRepeatRule.MONTHLY -> {
                            // plannedDates chứa các ngày lặp lại (dạng millis), kiểm tra nếu có ngày nào là cùng ngày trong tháng với hôm nay và trùng ngày
                            task.plannedDates.any {
                                val plannedCal = java.util.Calendar.getInstance()
                                plannedCal.timeInMillis = it
                                plannedCal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                                plannedCal.set(java.util.Calendar.MINUTE, 0)
                                plannedCal.set(java.util.Calendar.SECOND, 0)
                                plannedCal.set(java.util.Calendar.MILLISECOND, 0)
                                val todayCal = java.util.Calendar.getInstance()
                                todayCal.timeInMillis = today
                                plannedCal.get(java.util.Calendar.DAY_OF_MONTH) == todayCal.get(java.util.Calendar.DAY_OF_MONTH) &&
                                plannedCal.timeInMillis == todayCal.timeInMillis
                            }
                        }
                        com.example.worklifebalance.domain.model.TaskRepeatRule.CUSTOM -> {
                            task.plannedDates.any {
                                val cal = java.util.Calendar.getInstance()
                                cal.timeInMillis = it
                                cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                                cal.set(java.util.Calendar.MINUTE, 0)
                                cal.set(java.util.Calendar.SECOND, 0)
                                cal.set(java.util.Calendar.MILLISECOND, 0)
                                cal.timeInMillis == today
                            }
                        }
                        else -> false
                    }
                }
                else -> false
            }
        }
    }

    // Lấy danh sách goal đang tiến hành
    val executedDatesMap = tasks.associate { task ->
        task.id to executions.filter { it.taskId == task.id }.map { it.executionDate }
    }
    val inProgressGoals = remember(goals, tasks, executions) {
        val filtered = goals.filter { goal ->
            val type = goal.autoGoalType(tasks, executedDatesMap)
            Log.d(
                "DashboardGoal",
                "Goal: ${goal.name}, Progress: ${goal.progress(tasks, executedDatesMap) * 100}, Type: $type, Start: ${goal.startDate}, End: ${goal.endDate}, Now: ${System.currentTimeMillis()}"
            )
            type == com.example.worklifebalance.domain.model.GoalType.IN_PROGRESS.name
        }
        Log.d("DashboardGoal", "inProgressGoals count: ${filtered.size}")
        filtered
    }

    // State for difficulty filter
    var selectedDifficulty by remember { mutableStateOf<TaskDifficulty?>(null) }
    // Filter tasks by selected difficulty
    val filteredTodayTasks = remember(todayTasks, selectedDifficulty) {
        if (selectedDifficulty == null) todayTasks
        else todayTasks.filter { task ->
            TaskDifficulty.fromString(task.difficulty) == selectedDifficulty
        }
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
                    goalsCount = inProgressGoals.size,
                    tasksCount = todayTasks.size,
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
                                    modifier = Modifier.weight(1f).height(150.dp),
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
            if (todayTasks.isNotEmpty()) {
                item {
                    // Task difficulty filter bar
                    TaskFilterByDifficultyBar(
                        selectedFilter = selectedDifficulty,
                        onFilterSelected = { selectedDifficulty = it }
                    )
                }
                item {
                    TasksSection(
                        titleTasksSection = "Nhiệm vụ hôm nay",
                        tasks = filteredTodayTasks,
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
            } else {
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
                                text = "Nhiệm vụ hôm nay",
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

            // Hiển thị danh sách mục tiêu đang tiến hành
            if (inProgressGoals.isNotEmpty()) {
                item {
                    GoalsSection(
                        titleDisplay = "Mục tiêu đang tiến hành",
                        goals = inProgressGoals,
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
            } else {
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
                                text = "Mục tiêu đang tiến hành",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            EmptyPlaceholder(
                                title = "Chưa có dữ liệu mục tiêu.",
                                description = "Hãy bắt đầu bằng việc thêm một mà lĩnh vực bạn quan tâm.",
                            )
                        }
                    }
                }
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
    // Reminder Toast
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showReminderToast,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            ReminderToast(
                onDismiss = { showReminderToast = false },
                onSnooze = {
                    showReminderToast = false
                    coroutineScope.launch {
                        delay(5 * 60 * 1000)
                        showReminderToast = true
                    }
                },
                onStartRest = {
                    showReminderToast = false
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
            onAddTask = {
                showAddNewPopup = false
                showAddNewTaskPopup = true
            }
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
    if (showAddNewTaskPopup) {
        AddTaskDialog(
            domains = domains,
            goals= goals,
            onAdd = { task ->
                taskViewModel.insertTask(task)
                showAddNewTaskPopup = false
            },
            onDismiss = { showAddNewTaskPopup = false }
        )
    }
    // Reminder Toast theo khung giờ tùy chỉnh
    LaunchedEffect(reminderTimesReloadKey) {
        while (true) {
            // Đọc danh sách thời gian nhắc nhở từ SharedPreferences
            val sharedPref = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
            val gson = com.google.gson.Gson()
            val json = sharedPref.getString("reminder_times", null)
            val timeList: List<String> = if (json != null) {
                val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, String::class.java).type
                gson.fromJson(json, type)
            } else emptyList()
            val now = java.time.LocalDateTime.now()
            val today = now.toLocalDate()
            val reminderTimes = timeList.mapNotNull { t ->
                try {
                    val (h, m) = t.split(":").map { it.toInt() }
                    java.time.LocalDateTime.of(today, java.time.LocalTime.of(h, m))
                } catch (e: Exception) { null }
            }.sorted()
            val nextReminder = reminderTimes.firstOrNull { it.isAfter(now) }
            if (nextReminder != null) {
                val delayMillis = java.time.Duration.between(now, nextReminder).toMillis()
                delay(delayMillis)
                showReminderToast = true
            } else {
                // Chờ đến ngày mai
                val millisToTomorrow = java.time.Duration.between(
                    now,
                    now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
                ).toMillis()
                delay(millisToTomorrow)
            }
        }
    }

    var showConfirmDeleteGoal by remember { mutableStateOf(false) }
    var goalToDelete by remember { mutableStateOf<com.example.worklifebalance.domain.model.Goal?>(null) }
    // Dialog xác nhận xóa mục tiêu
    if (showConfirmDeleteGoal && goalToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDeleteGoal = false
                goalToDelete = null
            },
            title = { Text("Xác nhận xóa mục tiêu") },
            text = { Text("Bạn có chắc chắn muốn xóa mục tiêu này và tất cả nhiệm vụ liên quan không?") },
            confirmButton = {
                Button(onClick = {
                    goalToDelete?.let {
                        goalViewModel.deleteGoal(it)
                        taskViewModel.deleteTasksByGoalId(it.id)
                    }
                    showConfirmDeleteGoal = false
                    goalToDelete = null
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmDeleteGoal = false
                    goalToDelete = null
                }) { Text("Hủy") }
            }
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