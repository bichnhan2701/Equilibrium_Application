package com.example.worklifebalance.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.worklifebalance.ui.component.common.AddDomainDialog
import com.example.worklifebalance.ui.component.domain.DomainItem
import com.example.worklifebalance.ui.theme.*
import com.example.worklifebalance.ui.viewmodel.DomainViewModel
import com.example.worklifebalance.ui.viewmodel.GoalViewModel
import com.example.worklifebalance.domain.utils.getCurrentDate
import com.example.worklifebalance.ui.component.common.EditDomainDialog
import com.example.worklifebalance.ui.component.common.EmptyPlaceholder
import com.example.worklifebalance.ui.viewmodel.TaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainManagement(
    navController: NavController,
    viewModel: DomainViewModel = hiltViewModel(),
    goalViewModel: GoalViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val currentDate = getCurrentDate()

    val domains by viewModel.domains.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val goals by goalViewModel.goals.collectAsState()
    val context = LocalContext.current

    var showAddNewDomainPopup by remember { mutableStateOf(false) }
    var editingDomain by remember { mutableStateOf<com.example.worklifebalance.domain.model.Domain?>(null) }
    var deletingDomain by remember { mutableStateOf<com.example.worklifebalance.domain.model.Domain?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadDomains()
        goalViewModel.loadGoals()
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quản lý lĩnh vực",
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
                        onClick = { showAddNewDomainPopup = true },
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
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Danh sách lĩnh vực",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton (
                        onClick = { viewModel.deleteAllDomains() }
                    ) {
                        Text(
                            "Xóa tất cả",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PastelRed,
                        )
                    }
                }
            }
            item {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        domains.forEach { domain ->
                            DomainItem(
                                domain = domain,
                                goals = goals,
                                onDelete = { deletingDomain = domain },
                                onUpdate = { _, _ ->
                                    editingDomain = domain
                                }
                            )
                        }
                    }
                }
            }
            if( domains.isEmpty() && !isLoading) {
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
                            EmptyPlaceholder(
                                title = "Chưa có dữ liệu domain.",
                                description = "Hãy bắt đầu bằng việc thêm một mà lĩnh vực bạn quan tâm.",
                            )
                        }
                    }
                }
            }

        }
    }
    if (showAddNewDomainPopup) {
        AddDomainDialog(
            onAdd = { domain ->
                viewModel.addDomain(domain)
                showAddNewDomainPopup = false
            },
            onDismiss = { showAddNewDomainPopup = false }
        )
    }
    editingDomain?.let { domain ->
        EditDomainDialog(
            domain = domain,
            onEdit = { updatedDomain ->
                viewModel.updateDomain(updatedDomain.id, updatedDomain.name, updatedDomain.color.toULong())
                viewModel.loadDomains()
                editingDomain = null
            },
            onDismiss = { editingDomain = null }
        )
    }
    deletingDomain?.let { domain ->
        AlertDialog(
            onDismissRequest = { deletingDomain = null },
            title = { Text("Xác nhận xóa lĩnh vực") },
            text = { Text("Bạn có chắc chắn muốn xóa lĩnh vực '${domain.name}' không? Tất cả mục tiêu và nhiệm vụ liên quan cũng sẽ bị xóa.") },
            confirmButton = {
                Button(onClick = {
                    goalViewModel.deleteGoalsByDomainId(domain.id)
                    taskViewModel.deleteTasksByDomainId(domain.id)
                    viewModel.deleteDomainById(domain.id)
                    deletingDomain = null
                }) { Text("Xóa") }
            },
            dismissButton = {
                TextButton(onClick = { deletingDomain = null }) { Text("Hủy") }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun DomainManagementPreview() {
    WorkLifeBalanceTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            DomainManagement(navController = rememberNavController())
        }
    }
}
