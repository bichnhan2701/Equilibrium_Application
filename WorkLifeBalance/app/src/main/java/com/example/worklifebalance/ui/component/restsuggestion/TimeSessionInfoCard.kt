package com.example.worklifebalance.ui.component.restsuggestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.R
import com.example.worklifebalance.ui.theme.LightBlue
import com.example.worklifebalance.ui.theme.PastelBlue
import com.example.worklifebalance.ui.theme.PastelGreen
import com.example.worklifebalance.ui.theme.PastelRed
import com.example.worklifebalance.ui.theme.PastelYellow

@Composable
fun TimeSessionInfoCard(
    currentRestTime: Int,
    lastRestTime: String = "2 giờ trước",
    currentEnergy: Int
) {
    Card(
    modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp),
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
                    text = "Phiên nghỉ ngơi hiện tại",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.time_svgrepo_com),
                        contentDescription = null,
                        tint = PastelBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nghỉ ngơi hôm nay",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$currentRestTime phút",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "Nghỉ ngơi cuối",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = lastRestTime,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PastelRed
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val progressColor = when {
                currentEnergy >= 70 -> PastelGreen// Xanh lá
                currentEnergy >= 30 -> PastelYellow // Vàng
                else -> PastelRed // Đỏ nhạt
            }
            LinearProgressIndicator(
                progress = currentEnergy / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Năng lượng hiện tại: ${currentEnergy}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if(
                    currentEnergy < 30
                ) {
                    Text(
                        text = "Cần nghỉ ngơi",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = PastelRed
                    )
                }

            }
        }
    }
}