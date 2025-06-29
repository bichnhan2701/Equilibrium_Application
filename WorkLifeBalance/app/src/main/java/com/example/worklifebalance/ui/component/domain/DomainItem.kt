package com.example.worklifebalance.ui.component.domain

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Goal

@Composable
fun DomainItem(
    domain: Domain,
    goals: List<Goal>,
    onDelete: () -> Unit,
    onUpdate: (String, ULong) -> Unit
) {
    var name by remember { mutableStateOf(domain.name) }
    var color by remember { mutableStateOf(domain.color) }
    val domainGoals = goals.filter { it.domainId == domain.id }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(domain.color).copy(alpha =  0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = domain.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(domain.color)
                    )
                    Text(
                        text = "Số mục tiêu: ${domainGoals.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                var expandedMenu = remember { mutableStateOf(false) }
                IconButton(onClick = { expandedMenu.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Thêm tùy chọn",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DropdownMenu(
                        expanded = expandedMenu.value,
                        onDismissRequest = { expandedMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sửa") },
                            onClick = {
                                expandedMenu.value = false
                                onUpdate(name, color.toULong())
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa") },
                            onClick = {
                                expandedMenu.value = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Text(
                text = domain.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (domainGoals.isNotEmpty()) {
                domainGoals.forEach { goal ->
                    Text(
                        "- ${goal.name}",
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}