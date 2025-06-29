package com.example.worklifebalance.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.ui.theme.*

@Composable
fun EditDomainDialog(
    domain: Domain,
    onEdit: (Domain) -> Unit,
    onDismiss: () -> Unit
) {
    val domainColors = listOf(
        Orange, Lavender, Rose, LeafGreen, Brown, GreenYellow, SkyBlue, Violet, Pink, NeonPink, SeaBlue, Aqua
    )
    var editedDomainName by remember { mutableStateOf(domain.name) }
    var editedDomainColor by remember { mutableStateOf(domain.color) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Chỉnh sửa lĩnh vực",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = editedDomainName,
                    onValueChange = { editedDomainName = it },
                    label = { Text("Tên lĩnh vực") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(Modifier.height(8.dp))
                Text("Chọn màu:")
                LazyRow(modifier = Modifier.padding(end = 8.dp)) {
                    items(domainColors) { color ->
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(2.dp)
                                .border(
                                    width = if (editedDomainColor == color.value.toULong()) 2.dp else 1.dp,
                                    color = if (editedDomainColor == color.value.toULong()) Color.DarkGray else Color.LightGray,
                                    shape = MaterialTheme.shapes.small
                                )
                                .background(color, MaterialTheme.shapes.small)
                                .clickable { editedDomainColor = color.value.toULong() }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedDomain = domain.copy(
                        name = editedDomainName,
                        color = editedDomainColor
                    )
                    onEdit(updatedDomain)
                    onDismiss()
                },
                enabled = editedDomainName.isNotBlank()
            ) { Text("Lưu") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

