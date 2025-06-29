package com.example.worklifebalance.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.domain.model.RestEvent
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.model.sampleRecoveryActivities
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.restsuggestion.*
import com.example.worklifebalance.ui.viewmodel.EnergyViewModel
import com.example.worklifebalance.ui.viewmodel.RestSessionViewModel
import com.example.worklifebalance.ui.viewmodel.TaskViewModel
import java.time.LocalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestSuggestion(
    navController: NavController,
    restSessionViewModel: RestSessionViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    energyViewModel: EnergyViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    var showReminderToast by remember { mutableStateOf(true) }
    // Thêm biến lưu thời gian bắt đầu nghỉ
    var restStartTime by remember { mutableStateOf<Long?>(null) }
    var showRecoveryCard by remember { mutableStateOf(false) }
    var showRelaxTaskCard by remember { mutableStateOf(false) }
    var showCompletionMessage by remember { mutableStateOf(false) }
    var remainingTimeInSeconds by remember { mutableIntStateOf(300) }
    var showRecoveryCountdownTimer by remember { mutableStateOf(false) }
    var showRelaxCountdownTimer by remember { mutableStateOf(false) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var relaxTotalTimeInSeconds by remember { mutableIntStateOf(300) } // Thời gian thư giãn nhập từ RelaxTaskDetailCard

    // Effect to handle timer countdown for recovery activity
    LaunchedEffect(isTimerRunning, remainingTimeInSeconds, showRecoveryCountdownTimer) {
        if (showRecoveryCountdownTimer && isTimerRunning && remainingTimeInSeconds > 0) {
            delay(1000)
            remainingTimeInSeconds -= 1
        } else if (showRecoveryCountdownTimer && isTimerRunning && remainingTimeInSeconds <= 0) {
            isTimerRunning = false
            showRecoveryCountdownTimer = false
            showCompletionMessage = true
            delay(5000)
            showCompletionMessage = false
        }
    }
    // Effect to handle timer countdown for relax task
    LaunchedEffect(isTimerRunning, remainingTimeInSeconds, showRelaxCountdownTimer) {
        if (showRelaxCountdownTimer && isTimerRunning && remainingTimeInSeconds > 0) {
            delay(1000)
            remainingTimeInSeconds -= 1
        } else if (showRelaxCountdownTimer && isTimerRunning && remainingTimeInSeconds <= 0) {
            isTimerRunning = false
            showRelaxCountdownTimer = false
            showCompletionMessage = true
            delay(5000)
            showCompletionMessage = false
        }
    }

    val restHistory by restSessionViewModel.restHistory.collectAsState()
    val currentEnergy = energyViewModel.latestEnergy.collectAsState().value?.energy ?: 75
    val recoveryActivities = sampleRecoveryActivities
    var selectedActivity by remember { mutableStateOf<RecoveryActivity?>(null) }

    val tasks by taskViewModel.tasks.collectAsState()
    var selectedRelaxTask by remember { mutableStateOf<Task?>(null) }// Relax activities
    val relaxActivities = tasks.filter { it.difficulty == com.example.worklifebalance.domain.model.TaskDifficulty.RELAX.name }

    // restEvents lấy từ relaxActivities, sắp xếp theo plannedTime tăng dần (ví dụ: 7:00, 9:00, 12:30...)
    val restEvents = relaxActivities
        .sortedBy {
            try {
                LocalTime.parse(it.plannedTime)
            } catch (e: Exception) {
                LocalTime.MAX
            }
        }
        .map { task ->
            RestEvent(
                time = task.plannedTime,
                activityName = task.name
            )
        }

    // Lấy tổng thời gian nghỉ hôm nay
    val todaySessions = restHistory.flatMap { it.restSessions }
        .filter { it.time.toSecondOfDay() >= LocalTime.MIN.toSecondOfDay() && it.time.toSecondOfDay() <= LocalTime.MAX.toSecondOfDay() }
    val totalRestToday = todaySessions.sumOf { it.durationMinutes }
    // Lấy lần nghỉ gần nhất hôm nay
    val lastRest = todaySessions.maxByOrNull { it.time }?.time
    val lastRestDisplay = lastRest?.let {
        val now = LocalTime.now()
        val duration = java.time.Duration.between(it, now)
        when {
            duration.toMinutes() < 1 -> "Vừa xong"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} phút trước"
            duration.toHours() < 24 -> "${duration.toHours()} giờ trước"
            else -> "${duration.toDays()} ngày trước"
        }
    } ?: "Chưa nghỉ"

    LaunchedEffect(Unit) {
        restSessionViewModel.loadRestHistory()
        restSessionViewModel.loadTotalRestMinutes()
        taskViewModel.loadTasks()
        energyViewModel.loadLatestEnergy()
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
                            text = "Đề xuất nghỉ ngơi",
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
                    IconButton(
                        onClick = { /* TODO: Settings */ },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Cài đặt nhắc nhở",
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
                .padding(paddingValues)
                .padding(16.dp)
                .then(
                    if (showRecoveryCard || showRecoveryCountdownTimer || showRelaxCountdownTimer) {
                        Modifier.blur(8.dp).alpha(0.6f)
                    } else {
                        Modifier
                    }
                ),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            item {
                TimeSessionInfoCard(
                    currentRestTime = totalRestToday,
                    lastRestTime = lastRestDisplay,
                    currentEnergy = currentEnergy
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text(
                    text = "Hoạt động phục hồi gợi ý",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(recoveryActivities.size) { index ->
                        val activity = recoveryActivities[index]
                        RecoveryActivityCard(
                            activity = activity,
                            onClick = {
                                selectedActivity = activity
                                showRecoveryCard = true
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text(
                    text = "Hoạt động thư giãn của bạn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(relaxActivities.size) { index ->
                        val activity = relaxActivities[index]
                        RelaxActivityCard(
                            task = activity,
                            onClick = {
                                selectedRelaxTask = activity
                                showRelaxTaskCard = true
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text(
                    text = if (restEvents.isEmpty()) "Lịch nghỉ ngơi gợi ý" else "Lịch nghỉ ngơi của bạn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RestScheduleTimeline(restEvents)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Reminder Toast
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
                    showRecoveryCard = true
                }
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Reminder Toast
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
                }
            )
        }
    }
    Box (modifier = Modifier.fillMaxSize()) {
        // Completion Message
        AnimatedVisibility(
            visible = showCompletionMessage,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CompletionMessageCard()
        }
    }
    // Recovery Activity Detail Card
    if (showRecoveryCard && selectedActivity != null) {
        RecoveryActivityDetailCard(
            activity = selectedActivity!!,
            onDismiss = { showRecoveryCard = false },
            onStart = {
                showRecoveryCard = false
                remainingTimeInSeconds = selectedActivity!!.durationInMinutes * 60
                showRecoveryCountdownTimer = true
                isTimerRunning = true
                // Lưu thời gian bắt đầu nghỉ
                restStartTime = System.currentTimeMillis()
            }
        )
    }
    // Relax Task Detail Card
    if (showRelaxTaskCard && selectedRelaxTask != null) {
        RelaxTaskDetailCard(
            task = selectedRelaxTask!!,
            onDismiss = { showRelaxTaskCard = false },
            onStart = { minutes ->
                showRelaxTaskCard = false
                relaxTotalTimeInSeconds = minutes * 60
                remainingTimeInSeconds = relaxTotalTimeInSeconds
                showRelaxCountdownTimer = true
                isTimerRunning = true
                // Lưu thời gian bắt đầu nghỉ
                restStartTime = System.currentTimeMillis()
            }
        )
    }
    // Countdown Timer for relax task
    if (showRelaxCountdownTimer && selectedRelaxTask != null) {
        RelaxCountdownTimerDialog(
            remainingTimeInSeconds = remainingTimeInSeconds,
            totalTimeInSeconds = relaxTotalTimeInSeconds,
            task = selectedRelaxTask,
            onCancel = {
                showRelaxCountdownTimer = false
                isTimerRunning = false
                val elapsedMinutes = restStartTime?.let { ((System.currentTimeMillis() - it) / 60000).toInt().coerceAtLeast(1) } ?: (relaxTotalTimeInSeconds / 60)
                restSessionViewModel.addRestSession(
                    com.example.worklifebalance.domain.model.RestSession(
                        totalRestMinutes = elapsedMinutes,
                        restSessions = listOf(
                            com.example.worklifebalance.domain.model.RestSessionDetail(
                                durationMinutes = elapsedMinutes,
                                time = LocalTime.now()
                            )
                        )
                    )
                )
                restStartTime = null
            }
        )
    }
    // Countdown Timer for recovery activity
    if (showRecoveryCountdownTimer && selectedActivity != null) {
        CountdownTimerDialog(
            remainingTimeInSeconds = remainingTimeInSeconds,
            totalTimeInSeconds = selectedActivity?.durationInMinutes?.times(60) ?: 300,
            activity = selectedActivity,
            onCancel = {
                showRecoveryCountdownTimer = false
                isTimerRunning = false
                val elapsedMinutes = restStartTime?.let { ((System.currentTimeMillis() - it) / 60000).toInt().coerceAtLeast(1) } ?: (selectedActivity?.durationInMinutes ?: 5)
                restSessionViewModel.addRestSession(
                    com.example.worklifebalance.domain.model.RestSession(
                        totalRestMinutes = elapsedMinutes,
                        restSessions = listOf(
                            com.example.worklifebalance.domain.model.RestSessionDetail(
                                durationMinutes = elapsedMinutes,
                                time = LocalTime.now()
                            )
                        )
                    )
                )
                restStartTime = null
            }
        )
    }
}