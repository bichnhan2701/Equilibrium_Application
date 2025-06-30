package com.example.worklifebalance.ui.component.restsuggestion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.RecoveryActivity
import java.util.UUID

@Composable
fun AddRecoveryActivityDialog(
    onAdd: (RecoveryActivity) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf(15) }
    var durationText by remember { mutableStateOf("15") }
    var color by remember { mutableStateOf(Color(0xFF81C784)) }
    val colorOptions = listOf(
        Color(0xFF81C784), // Xanh lá
        Color(0xFF64B5F6), // Xanh dương
        Color(0xFFFFB74D), // Cam
        Color(0xFF9575CD), // Tím
        Color(0xFF4FC3F7), // Xanh nước biển
        Color(0xFFFF8A65), // Cam nhạt
        Color(0xFFA1887F), // Nâu nhạt
        Color(0xFF90A4AE), // Xám xanh
        Color(0xFFBA68C8), // Tím nhạt
        Color(0xFF4DD0E1), // Xanh ngọc
        Color(0xFF388E3C), // Xanh đậm
        Color(0xFFF06292), // Hồng
        Color(0xFFFFD54F), // Vàng
        Color(0xFF009688), // Xanh teal
        Color(0xFFD4E157)  // Vàng nhạt
    )
    val isValid = name.isNotBlank() && duration > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm hoạt động thư giãn") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên hoạt động") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = durationText,
                    onValueChange = {
                        durationText = it.filter { c -> c.isDigit() }
                        duration = durationText.toIntOrNull() ?: 0
                    },
                    label = { Text("Thời lượng (phút)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(Modifier.height(8.dp))
                Text("Chọn màu:")
                LazyRow {
                    colorOptions.forEach { option ->
                        item {
                            IconButton(
                                onClick = { color = option },
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(2.dp)
                            ) {
                                Icon(
                                    imageVector = if (color == option) Icons.Default.CheckCircle else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = option
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        onAdd(
                            RecoveryActivity(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                description = description,
                                durationInMinutes = duration,
                                color = color
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = isValid
            ) { Text("Thêm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}
