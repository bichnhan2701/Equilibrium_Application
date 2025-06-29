package com.example.worklifebalance.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.ui.theme.*

@Composable
fun AddDomainDialog(
    onAdd: (Domain) -> Unit,
    onDismiss: () -> Unit
) {
    val domainColors = listOf(
        Orange, Lavender, Rose, LeafGreen, Brown, GreenYellow, SkyBlue, Violet, Pink, NeonPink, SeaBlue, Aqua
    )
    var newDomainName by remember { mutableStateOf("") }
    var newDomainColor by remember { mutableStateOf(0xFF2196F3UL) } // Long

    AlertDialog(
    onDismissRequest = onDismiss,
    title = {
        Text(
            "Thêm lĩnh vực mới",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp
        )
    },
    text = {
        Column {
            OutlinedTextField(
                value = newDomainName,
                onValueChange = { newDomainName = it },
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
                                width = if (newDomainColor == color.value.toULong()) 2.dp else 1.dp,
                                color = if (newDomainColor == color.value.toULong()) Color.DarkGray else Color.LightGray,
                                shape = MaterialTheme.shapes.small
                            )
                            .background(color, MaterialTheme.shapes.small)
                            .clickable { newDomainColor = color.value.toULong() }
                    )
                }
            }
        }
    },
    confirmButton = {
        Button(
            onClick = {
                val domain = Domain(
                    id = java.util.UUID.randomUUID().toString(),
                    name = newDomainName,
                    color = newDomainColor
                )
                onAdd(domain)
                newDomainName = ""
                newDomainColor = 0xFF2196F3UL // reset as ULong
                onDismiss()
            },
            enabled = newDomainName.isNotBlank()
        ) { Text("Thêm") }
    },
    dismissButton = {
        TextButton(onClick = onDismiss) { Text("Hủy") }
    }
    )
}