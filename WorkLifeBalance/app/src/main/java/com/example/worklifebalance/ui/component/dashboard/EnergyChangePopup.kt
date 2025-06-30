package com.example.worklifebalance.ui.component.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.ui.theme.*

@Composable
fun EnergyChangePopup(energy: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            energy < 30 -> LightRed
                            energy < 70 -> LightYellow
                            else -> LightGreen
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        energy < 30 -> Icons.Filled.Face
                        energy < 70 -> Icons.Filled.Face
                        else -> Icons.Filled.Face
                    },
                    contentDescription = null,
                    tint = when {
                        energy < 30 -> PastelRed
                        energy < 70 -> PastelYellow
                        else -> PastelGreen
                    },
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when {
                        energy < 30 -> "Năng lượng thấp"
                        energy < 70 -> "Năng lượng vừa phải"
                        else -> "Năng lượng cao"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        energy < 30 -> "Bạn nên thư giãn để phục hồi năng lượng!"
                        energy < 70 -> "Bạn nên cân bằng giữa công việc và thư giãn!"
                        else -> "Thời điểm tốt để hoàn thành các nhiệm vụ quan trọng!"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}