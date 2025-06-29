package com.example.worklifebalance.ui.component.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Domain

@Composable
fun GoalFilterByDomainBar(
    domains: List<Domain>,
    selectedFilter: Domain?,
    onFilterSelected: (Domain?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Lĩnh vực",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.2f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedFilter == null) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onFilterSelected(null) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tất cả",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedFilter == null) Color.White else Color.Gray,
                        fontWeight = if (selectedFilter == null) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            domains.forEach { domain ->
                item(domain.id) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedFilter == domain) Color(domain.color) else Color.Transparent)
                            .clickable { onFilterSelected(domain) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = domain.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedFilter == domain) Color.White else Color.Gray,
                            fontWeight = if (selectedFilter == domain) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}