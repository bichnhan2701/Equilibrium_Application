package com.example.worklifebalance.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.navigation.Screen
import com.example.worklifebalance.ui.component.common.AddGoalDialog
import com.example.worklifebalance.ui.component.common.EmptyPlaceholder
import com.example.worklifebalance.ui.component.common.GoalsSection
import com.example.worklifebalance.ui.component.goal.GoalFilterByDomainBar
import com.example.worklifebalance.ui.viewmodel.GoalViewModel
import com.example.worklifebalance.ui.viewmodel.DomainViewModel
import com.example.worklifebalance.ui.viewmodel.TaskExecutionViewModel
import com.example.worklifebalance.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun GoalManagement(
    navController: NavController,
    goalViewModel: GoalViewModel = hiltViewModel(),
    domainViewModel: DomainViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    taskExecutionViewModel: TaskExecutionViewModel = hiltViewModel()
) {
    val currentDate = getCurrentDate()
    val context = LocalContext.current

    val goals by goalViewModel.goals.collectAsState()
    val domains by domainViewModel.domains.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()
    val executions by taskExecutionViewModel.executions.collectAsState()

    val isLoading by goalViewModel.isLoading.collectAsState()
    val error by goalViewModel.error.collectAsState()
    val (selectedFilter, setSelectedFilter) = remember { mutableStateOf<Domain?>(null) }

    var showAddNewGoalPopup by remember { mutableStateOf(false) }
    var showConfirmDeletingAllGoal by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        domainViewModel.loadDomains()
        goalViewModel.loadGoals()
        taskViewModel.loadTasks()
        taskExecutionViewModel.loadAllExecutions()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quản lý mục tiêu",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { showAddNewGoalPopup = true },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoalFilterByDomainBar(
                domains = domains,
                selectedFilter = selectedFilter,
                onFilterSelected = { setSelectedFilter(it) }
            )
            LazyColumn {
                item {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        val executedDatesMap = tasks.associate { task ->
                            task.id to executions.filter { it.taskId == task.id }.map { it.executionDate }
                        }
                        GoalsSection(
                            titleDisplay = "Danh sách mục tiêu",
                            goals = goals.filter { it.domainId == selectedFilter?.id || selectedFilter == null },
                            domains = domains,
                            onGoalClick = { goalId ->
                                navController.navigate(Screen.GoalDetailManagement.goalDetailManagementRoute(goalId))
                            },
                            onUpdateGoal =  { goal ->
                                goalViewModel.updateGoal(goal)
                            },
                            onDeleteGoal = { goal ->
                                goalViewModel.deleteGoal(goal)
                            },
                            onViewOrDeleteAll = {
                                showConfirmDeletingAllGoal = true
                            },
                            viewOrDeleteAllText = "Xóa tất cả",
                            tasks = tasks,
                            executedDatesMap = executedDatesMap
                        )
                    }
                    Divider(modifier = Modifier.padding(16.dp))
                }
                if( goals.isEmpty()) {
                    item {
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
                                    text = "Danh sách mục tiêu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
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

    if(showConfirmDeletingAllGoal) {
        AlertDialog(
            onDismissRequest = { showConfirmDeletingAllGoal = false },
            title = { Text("Xác nhận xóa mục tiêu") },
            text = { Text("Bạn có chắc chắn muốn xóa tất cả mục tiêu không?") },
            confirmButton = {
                Button(onClick = {
                    goalViewModel.deleteAllGoals()
                    showConfirmDeletingAllGoal = false
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeletingAllGoal = false }) { Text("Hủy") }
            }
        )
    }
}
