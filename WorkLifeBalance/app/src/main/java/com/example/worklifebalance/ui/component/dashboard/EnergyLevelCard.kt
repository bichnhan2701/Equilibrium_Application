package com.example.worklifebalance.ui.component.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.R

@Composable
fun EnergyLevelCard(
    modifier: Modifier = Modifier,
    onEditEnergy: () -> Unit = {},
    energy: Int,
    updateTime: String = "Chưa cập nhật"
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Năng lượng",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable(onClick = onEditEnergy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_3_svgrepo_com),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$energy%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
//            val progressColor = when {
//                energy < 30 -> listOf(Color(0xFFFFCDD2), Color(0xFFE57373))
//                energy < 70 -> listOf(Color(0xFFFFE0B2), Color(0xFFFFB74D))
//                else -> listOf(Color(0xFFC8E6C9), Color(0xFF81C784))
//            }
//            LinearProgressIndicator(
//                progress = energy / 100f,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp)
//                    .clip(RoundedCornerShape(4.dp)),
//                color = progressColor.first(), // Use the first color as the indicator color
//                trackColor = Color.LightGray.copy(alpha = 0.3f)
//            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(energy.toFloat() / 100f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = when {
                                    energy < 30 -> listOf(
                                        Color(0xFFFFCDD2),
                                        Color(0xFFE57373)
                                    )
                                    energy < 70 -> listOf(
                                        Color(0xFFFFE0B2),
                                        Color(0xFFFFB74D)
                                    )
                                    else -> listOf(
                                        Color(0xFFC8E6C9),
                                        Color(0xFF81C784)
                                    )
                                }
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.time_svgrepo_com),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = updateTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}