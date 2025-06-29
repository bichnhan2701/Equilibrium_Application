package com.example.worklifebalance.ui.component.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.ui.theme.PastelPurple
import com.example.worklifebalance.ui.theme.WorkLifeBalanceTheme

@Composable
fun EnergyRecording(
    energy: Int,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onEnergyChange: (Int) -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Ghi lại mức năng lượng hiện tại",
                style = MaterialTheme.typography.titleMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        },
        text = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                // Energy Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(energy / 100f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = when {
                                        energy < 30 -> listOf(Color(0xFFFFCDD2), Color(0xFFE57373))
                                        energy < 70 -> listOf(Color(0xFFFFE0B2), Color(0xFFFFB74D))
                                        else -> listOf(Color(0xFFC8E6C9), Color(0xFF81C784))
                                    }
                                )
                            )
                    )
                    Text(
                        text = "$energy%",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Energy Level Slider
                Slider(
                    value = energy.toFloat(),
                    onValueChange = { onEnergyChange(it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 0,
                    colors = SliderDefaults.colors(
                        thumbColor = PastelPurple,
                        activeTrackColor = PastelPurple,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
                // Energy Level Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Thấp",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Trung bình",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Cao",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text(
                    "Xác nhận",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(
                    "Bỏ qua",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}

@Preview(showBackground = true)
@Composable
fun EnergyRecordingPreview() {
    WorkLifeBalanceTheme {
        EnergyRecording(
            energy = 75,
            onDismissRequest = {},
            onConfirm = {},
            onEnergyChange = {}
        )
    }

}