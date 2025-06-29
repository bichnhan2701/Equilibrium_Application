package com.example.worklifebalance.ui.component.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.collections.forEach
import kotlin.math.*
import kotlin.ranges.until
import kotlin.text.substring

@Composable
fun EnergyHeatmapChart(
    energyList: List<Energy>,
) {
    val today = LocalDate.now()
    // Chuyển energyList thành energyData: Map<LocalDate, Int>
    val energyData = energyList.associate { energy ->
        val date = java.time.Instant.ofEpochMilli(energy.updatedAt).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        date to energy.energy
    }

    // Luôn hiển thị tháng hiện tại
    val startOfMonth = today.withDayOfMonth(1)
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
    val startDate = startOfMonth
    val endDate = endOfMonth

    val daysToShow = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
    val weeksToShow = ceil(daysToShow / 7.0).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Day of week headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 4.dp)
            ) {
                val daysOfWeek = listOf(
                    DayOfWeek.SUNDAY,
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY
                )
                daysOfWeek.forEach { day ->
                    Text(
                        text = day.name.substring(0, 1),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Heatmap grid
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (weekIndex in 0 until weeksToShow) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        for (dayOfWeek in 0..6) {
                            val cellDate = startDate.plusDays((weekIndex * 7 + dayOfWeek).toLong())
                            if (cellDate.isBefore(startDate) || cellDate.isAfter(endDate)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                )
                            } else {
                                val energyLevel = energyData[cellDate] ?: -1
                                val color = when {
                                    energyLevel == -1 -> Color.LightGray.copy(alpha = 0.5f)
                                    energyLevel <= 30 -> Red.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    energyLevel in 31..70 -> Yellow.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    energyLevel in 71..100 -> Green.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    else -> Color.LightGray.copy(alpha = 0.5f)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                        .background(color)
                                        .border(
                                            width = if (cellDate == today) 2.dp else 0.dp,
                                            color = if (cellDate == today) Color.DarkGray else Color.Transparent,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                4.dp
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cellDate.dayOfMonth.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Năng lượng thấp",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Color gradient
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(8.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Red.copy(alpha = 0.7f),
                                    Orange.copy(alpha = 0.8f),
                                    Yellow,
                                    Green.copy(alpha = 0.8f),
                                    PastelGreen
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Năng lượng cao",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}