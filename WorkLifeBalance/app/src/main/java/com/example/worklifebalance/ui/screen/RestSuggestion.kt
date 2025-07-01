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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.domain.model.sampleRecoveryActivities
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.common.EmptyPlaceholder
import com.example.worklifebalance.ui.component.restsuggestion.*
import com.example.worklifebalance.ui.viewmodel.*
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
    recoveryActivityViewModel: RecoveryActivityViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var showReminderToast by remember { mutableStateOf(false) }
    var restStartTime by remember { mutableStateOf<Long?>(null) }
    var remainingTimeInSeconds by remember { mutableIntStateOf(300) }

    var showRecoveryCard by remember { mutableStateOf(false) }
    var showCompletionMessage by remember { mutableStateOf(false) }
    var showRecoveryCountdownTimer by remember { mutableStateOf(false) }
    var showRelaxCountdownTimer by remember { mutableStateOf(false) }

    var isTimerRunning by remember { mutableStateOf(false) }

    // Biến trạng thái cho ReminderTimeDialog
    var showReminderTimeDialog by remember { mutableStateOf(false) }

    // State to trigger reload of reminder times
    var reminderTimesReloadKey by remember { mutableStateOf(0) }

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
    val currentEnergy = energyViewModel.latestEnergy.collectAsState().value
    val userRecoveryActivities = recoveryActivityViewModel.recoveryActivities.collectAsState().value
    val recoveryActivities = sampleRecoveryActivities
    var selectedActivity by remember { mutableStateOf<RecoveryActivity?>(null) }

    // Lấy tổng thời gian nghỉ hôm nay
    val today = java.time.LocalDate.now()
    val todaySessions = restHistory.flatMap { it.restSessions }
        .filter { it.dateTime.toLocalDate() == today }
    val totalRestToday = todaySessions.sumOf { it.durationMinutes }
    // Lấy lần nghỉ gần nhất (dùng full date-time)
    val allSessions = restHistory.flatMap { it.restSessions }
    val lastRestDateTime = allSessions.maxByOrNull { it.dateTime }?.dateTime
    val lastRestDisplay = lastRestDateTime?.let {
        val now = java.time.LocalDateTime.now()
        val duration = java.time.Duration.between(it, now)
        when {
            duration.toMinutes() < 1 -> "Vừa xong"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} phút trước"
            duration.toHours() < 24 -> "${duration.toHours()} giờ trước"
            else -> "${duration.toDays()} ngày trước"
        }
    } ?: "Chưa nghỉ"

    var showAddRecoveryActivityDialog by remember { mutableStateOf(false) }

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
                        onClick = { showReminderTimeDialog = true },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Cài đặt nhắc nhở",
                            tint = MaterialTheme.colorScheme.onSurface
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
                    currentEnergy = currentEnergy?.energy ?: 0
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hoạt động của bạn",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(
                        onClick = {
                            showAddRecoveryActivityDialog = true
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Thêm",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                if( userRecoveryActivities.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        EmptyPlaceholder(
                            title = "Bạn chưa thêm hoạt động thư giãn nào.",
                            description = "Hãy thêm hoạt động thư giãn mà bạn thích hoặc thói quen nghỉ ngơi của bạn.",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                showAddRecoveryActivityDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            ),
                        ) {
                            Text(text = "Thêm hoạt động thư giãn")
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        items(userRecoveryActivities.size) { index ->
                            val activity = userRecoveryActivities[index]
                            RecoveryActivityCard(
                                activity = activity,
                                onClick = {
                                    selectedActivity = activity
                                    showRecoveryCard = true
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text(
                    text = "Lịch trình gợi ý",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RestScheduleTimeline()
            }
        }
        if (showAddRecoveryActivityDialog) {
            AddRecoveryActivityDialog(
                onAdd = { recoveryActivity ->
                    recoveryActivityViewModel.insertRecoveryActivity(recoveryActivity)
                    showAddRecoveryActivityDialog = false
                },
                onDismiss = { showAddRecoveryActivityDialog = false }
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
                                time = LocalTime.now(),
                                dateTime = java.time.LocalDateTime.now()
                            )
                        )
                    )
                )
                restStartTime = null
            }
        )
    }

    // Reminder Toast theo khung giờ tùy chỉnh
    val context = LocalContext.current
    LaunchedEffect(reminderTimesReloadKey) {
        while (true) {
            // Đọc danh sách thời gian nhắc nhở từ SharedPreferences
            val sharedPref = context.getSharedPreferences("reminder_prefs", android.content.Context.MODE_PRIVATE)
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
    // Hiển thị ReminderTimeDialog khi cần
    if (showReminderTimeDialog) {
        com.example.worklifebalance.ui.component.ReminderTimeDialog(
            showDialog = showReminderTimeDialog,
            onDismiss = { showReminderTimeDialog = false },
            onSaved = {
                // Tăng key để trigger reload reminder times nếu cần
                reminderTimesReloadKey++
            }
        )
    }
}