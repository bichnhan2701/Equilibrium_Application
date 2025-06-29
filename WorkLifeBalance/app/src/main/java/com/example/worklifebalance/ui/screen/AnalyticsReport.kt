package com.example.worklifebalance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.analytics.EnergyHeatmapChart
import com.example.worklifebalance.ui.component.analytics.ProgressSummaryCard
import com.example.worklifebalance.ui.component.analytics.TaskAllocationDetailDialog
import com.example.worklifebalance.ui.component.analytics.TaskAllocationPieChart
import com.example.worklifebalance.ui.theme.*
import com.example.worklifebalance.ui.viewmodel.DomainViewModel
import com.example.worklifebalance.ui.viewmodel.EnergyViewModel
import com.example.worklifebalance.ui.viewmodel.GoalViewModel
import com.example.worklifebalance.ui.viewmodel.TaskExecutionViewModel
import com.example.worklifebalance.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsReport(
    taskViewModel: TaskViewModel = hiltViewModel(),
    goalViewModel: GoalViewModel = hiltViewModel(),
    domainViewModel: DomainViewModel = hiltViewModel(),
    taskExecutionViewModel: TaskExecutionViewModel = hiltViewModel(),
    energyViewModel: EnergyViewModel = hiltViewModel(),
) {
    val currentDate = getCurrentDate()

    val domains by domainViewModel.domains.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()
    val goals by goalViewModel.goals.collectAsState()
    val executions by taskExecutionViewModel.executions.collectAsState()
    val energyList by energyViewModel.energies.collectAsState()

    var showTaskAllocationDetail by remember { mutableStateOf(false) }
    var selectedDomain by remember { mutableStateOf<Domain?>(null) }

    LaunchedEffect(Unit) {
        energyViewModel.loadAllEnergy()
        energyViewModel.loadLatestEnergy()
        taskViewModel.loadTasks()
        domainViewModel.loadDomains()
        goalViewModel.loadGoals()
        taskExecutionViewModel.loadAllExecutions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Phân tích & Báo cáo",
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
                    IconButton(
                        onClick = { /* TODO: Settings */ },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Cài đặt",
                            tint = PastelPurple
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Progress Summary
            item {
                val executedDatesMap = executions.groupBy { it.taskId }
                    .mapValues { entry -> entry.value.map { it.executionDate } }
                ProgressSummaryCard(
                    goals = goals,
                    tasks = tasks,
                    executedDatesMap = executedDatesMap
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            // Time Allocation
            item {
                Text(
                    text = "Phân bổ nhiệm vụ theo lĩnh vực",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                TaskAllocationPieChart(
                    domains = domains,
                    tasks = tasks,
                    onDomainSelected = {
                        selectedDomain = it
                        showTaskAllocationDetail = true
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            // Energy & Emotion Charts
            item {
                Text(
                    text = "Phân bổ nhiệm vụ theo lĩnh vực",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                EnergyHeatmapChart(energyList = energyList)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        // Task Allocation Detail Dialog
        if (showTaskAllocationDetail && selectedDomain != null) {
            TaskAllocationDetailDialog(
                domain = selectedDomain!!,
                tasks = tasks,
                onDismiss = { showTaskAllocationDetail = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsReportPreView() {
    AnalyticsReport()
}