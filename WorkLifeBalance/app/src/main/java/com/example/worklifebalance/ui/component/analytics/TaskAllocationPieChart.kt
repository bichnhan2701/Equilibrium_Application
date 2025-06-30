package com.example.worklifebalance.ui.component.analytics

import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.ui.theme.PastelPurple
import kotlin.collections.sumOf
import kotlin.math.*
import kotlin.math.roundToInt

@Composable
fun TaskAllocationPieChart(
    domains: List<Domain>,
    tasks: List<Task>,
    onDomainSelected: (Domain) -> Unit
) {
    val domainTaskCounts = domains.map { domain ->
        domain to tasks.count { it.domainId == domain.id }
    }
    val totalTasks = domainTaskCounts.sumOf { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pie chart
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val center = Offset(
                                    size.width / 2f,
                                    size.height / 2f
                                )
                                val radius = minOf(size.width, size.height) / 2
                                val angle = atan2(
                                    offset.y - center.y,
                                    offset.x - center.x
                                ) * (180 / PI).toFloat()
                                val normalizedAngle = (angle + 360) % 360
                                var startAngle = 0f
                                for ((domain, count) in domainTaskCounts) {
                                    val sweepAngle = if (totalTasks > 0) (count / totalTasks.toFloat()) * 360f else 0f
                                    if (normalizedAngle >= startAngle && normalizedAngle < startAngle + sweepAngle) {
                                        onDomainSelected(domain)
                                        break
                                    }
                                    startAngle += sweepAngle
                                }
                            }
                        }
                ) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = minOf(size.width, size.height) / 2 * 0.8f
                    var startAngle = 0f
                    domainTaskCounts.forEach { (domain, count) ->
                        val sweepAngle = if (totalTasks > 0) (count / totalTasks.toFloat()) * 360f else 0f
                        if (count > 0) {
                            drawArc(
                                color = Color(domain.color),
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2, radius * 2)
                            )
                        }
                        startAngle += sweepAngle
                    }
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.6f,
                        center = center
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalTasks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PastelPurple
                    )
                    Text(
                        text = "nhiệm vụ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Legend
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                domainTaskCounts.forEach { (domain, count) ->
                    if (count > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onDomainSelected(domain) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color(domain.color), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = domain.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$count nhiệm vụ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(domain.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val percent = if (totalTasks > 0) ((count / totalTasks.toFloat()) * 100).roundToInt() else 0
                            Text(
                                text = "$percent%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nhấn vào từng phần để xem chi tiết",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            val leastFocusedDomain = if (domains.isNotEmpty()) {
                val domainTaskCounts = domains.associateWith { domain ->
                    tasks.count { it.domainId == domain.id }
                }
                val minCount = domainTaskCounts.values.minOrNull() ?: 0
                domainTaskCounts.filter { it.value == minCount }.keys.toList()
            } else emptyList()
            Spacer(modifier = Modifier.height(16.dp))
            if (leastFocusedDomain.isNotEmpty()) {
                val names = leastFocusedDomain.joinToString { it.name }
                Text(
                    text = "Bạn ít tập trung vào lĩnh vực: $names. Hãy cân nhắc dành thêm thời gian cho chúng nhé.",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Không có lĩnh vực nào để đánh giá",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}