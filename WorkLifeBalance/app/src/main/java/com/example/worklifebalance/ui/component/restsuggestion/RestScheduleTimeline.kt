package com.example.worklifebalance.ui.component.restsuggestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.RestEvent
import com.example.worklifebalance.ui.theme.PastelGreen
import kotlin.collections.forEachIndexed

@Composable
fun RestScheduleTimeline() {
    val displayEvents = listOf(
        RestEvent("Tập thể dục hoặc yoga buổi sáng", "06:00"),
        RestEvent("Giãn cơ hoặc đi dạo ngắn giữa buổi sáng", "10:00"),
        RestEvent("Ăn trưa, nghỉ ngơi", "12:00"),
        RestEvent("Ngủ trưa ngắn", "12:30"),
        RestEvent("Uống trà hay cà phê, ăn nhẹ", "15:00"),
        RestEvent("Đi bộ hoặc chăm sóc cây xanh", "16:30"),
        RestEvent("Ăn tối, nghỉ ngơi", "19:00"),
        RestEvent("Đọc sách hay nghe nhạc", "21:00"),
        RestEvent("Thiền", "22:00"),
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            displayEvents.forEachIndexed { index, event ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.activityName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = event.time,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (index < displayEvents.size - 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(24.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}