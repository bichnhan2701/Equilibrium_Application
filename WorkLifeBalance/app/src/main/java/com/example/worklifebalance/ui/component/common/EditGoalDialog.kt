package com.example.worklifebalance.ui.component.common

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Goal
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
@Composable
fun EditGoalDialog(
    context: android.content.Context,
    domains: List<Domain>,
    goal: Goal,
    onUpdate: (Goal) -> Unit,
    onDismiss: () -> Unit,
) {
    var expandedDomain by remember { mutableStateOf(false) }
    var selectedDomainId by remember { mutableStateOf(goal.domainId) }
    var newGoalName by remember { mutableStateOf(goal.name) }
    var newGoalDescription by remember { mutableStateOf(goal.description ?: "") }
    var newGoalStartDate by remember { mutableLongStateOf(goal.startDate) }
    var newGoalEndDate by remember { mutableLongStateOf(goal.endDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Chỉnh sửa mục tiêu",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        },
        text = {
            Column {
                Box(modifier = Modifier.zIndex(1f)) {
                    OutlinedButton(onClick = { expandedDomain = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(domains.find { it.id == selectedDomainId }?.name ?: "Chọn lĩnh vực")
                    }
                    DropdownMenu(
                        expanded = expandedDomain,
                        onDismissRequest = { expandedDomain = false },
                        modifier = Modifier.zIndex(2f)
                    ) {
                        domains.forEach { domain ->
                            DropdownMenuItem(text = { Text(domain.name) }, onClick = {
                                selectedDomainId = domain.id
                                expandedDomain = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newGoalName,
                    onValueChange = { newGoalName = it },
                    label = { Text("Tên mục tiêu") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newGoalDescription,
                    onValueChange = { newGoalDescription = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(Modifier.height(8.dp))

                val startDateText = if (newGoalStartDate > 0) java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(newGoalStartDate)) else "Start Date"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ngày bắt đầu:",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Button(onClick = {
                        val cal = Calendar.getInstance()
                        if (newGoalStartDate > 0) cal.timeInMillis = newGoalStartDate
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                cal.set(year, month, dayOfMonth, 0, 0, 0)
                                newGoalStartDate = cal.timeInMillis
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }, modifier = Modifier.fillMaxWidth()) { Text(startDateText) }
                }

                val endDateText = if (newGoalEndDate > 0) java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(newGoalEndDate)) else "End Date"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ngày kết thúc:",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Button(onClick = {
                        val cal = Calendar.getInstance()
                        if (newGoalEndDate > 0) cal.timeInMillis = newGoalEndDate
                        DatePickerDialog(context, { _, year, month, dayOfMonth ->
                            cal.set(year, month, dayOfMonth, 0, 0, 0)
                            newGoalEndDate = cal.timeInMillis
                        },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }, modifier = Modifier.fillMaxWidth()) { Text(endDateText) }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newGoalName.isNotBlank() && selectedDomainId.isNotBlank() && newGoalStartDate > 0L && newGoalEndDate > 0L) {
                        val updatedGoal = goal.copy(
                            domainId = selectedDomainId,
                            name = newGoalName,
                            description = newGoalDescription,
                            startDate = newGoalStartDate,
                            endDate = newGoalEndDate,
                            updatedAt = System.currentTimeMillis()
                        )
                        onUpdate(updatedGoal)
                        onDismiss()
                    }
                }
            ) {
                Text("Cập nhật")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}



