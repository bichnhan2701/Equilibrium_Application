package com.example.worklifebalance.ui.component.analytics

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
    // Chuyển energyList thành energyData: Map<LocalDate, Float> (năng lượng trung bình mỗi ngày)
    val energyData = energyList
        .groupBy { energy ->
            java.time.Instant.ofEpochMilli(energy.updatedAt).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        }
        .mapValues { entry ->
            val energies = entry.value.map { it.energy }
            energies.average().toFloat()
        }

    // Luôn hiển thị tháng hiện tại
    val startOfMonth = today.withDayOfMonth(1)
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
    val startDate = startOfMonth
    val endDate = endOfMonth

    val daysToShow = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
    val weeksToShow = ceil(daysToShow / 7.0).toInt()

    // Tính năng lượng trung bình trong tháng hiện tại
    val monthAverages = energyData.filterKeys { it.month == today.month && it.year == today.year }
    val monthAverage = if (monthAverages.isNotEmpty()) monthAverages.values.average().toInt() else 0

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
            val firstDayOfWeek = startOfMonth.dayOfWeek.value % 7 // 0=Sunday, 1=Monday, ..., 6=Saturday
            val daysInMonth = today.lengthOfMonth()
            val totalCells = ceil((firstDayOfWeek + daysInMonth) / 7.0).toInt() * 7
            var dayCounter = 1
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (cellIndex in 0 until totalCells step 7) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        for (dayOfWeek in 0..6) {
                            val gridIndex = cellIndex + dayOfWeek
                            if (gridIndex < firstDayOfWeek || dayCounter > daysInMonth) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                )
                            } else {
                                val cellDate = startOfMonth.plusDays((dayCounter - 1).toLong())
                                val energyLevel = (energyData[cellDate] ?: -1).toFloat()
                                val color = when {
                                    energyLevel == -1f -> Color.LightGray.copy(alpha = 0.5f)
                                    energyLevel <= 30f -> Red.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    energyLevel in 31f..70f -> Yellow.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    energyLevel in 71f..100f -> Green.copy(alpha = 0.2f + (energyLevel / 100f) * 0.8f)
                                    else -> Color.LightGray.copy(alpha = 0.5f)
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .background(color, shape = MaterialTheme.shapes.small)
                                ) {
                                    Text(
                                        text = dayCounter.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                dayCounter++
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

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mức năng lượng trung bình của bạn trong tháng này là: $monthAverage%.",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val advice = when {
                monthAverage <= 30 -> "Năng lượng trung bình của bạn ở mức thấp, bạn nên dành thời gian nghỉ ngơi, thư giãn và ngủ đủ giấc để không ảnh hưởng đến việc hoàn thành nhiệm vụ nhé!"
                monthAverage in 31..70 -> "Năng lượng ở mức trung bình, hãy duy trì thói quen sinh hoạt lành mạnh và cân bằng giữa công việc và nghỉ ngơi."
                monthAverage > 70 -> "Năng lượng trung bình của bạn ở mức cao, bạn có thể tiếp tục phát huy, thử thách bản thân với các mục tiêu mới!"
                else -> "Không đủ dữ liệu để đưa ra lời khuyên."
            }
            Text(
                text = advice,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}