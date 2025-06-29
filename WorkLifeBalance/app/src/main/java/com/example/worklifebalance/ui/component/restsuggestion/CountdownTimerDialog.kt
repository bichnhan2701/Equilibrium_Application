package com.example.worklifebalance.ui.component.restsuggestion

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.ui.theme.PastelBlue
import kotlin.text.format

@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimerDialog(
    remainingTimeInSeconds: Int,
    totalTimeInSeconds: Int,
    activity: RecoveryActivity?,
    onCancel: () -> Unit
) {
    val progress = remainingTimeInSeconds.toFloat() / totalTimeInSeconds.toFloat()
    val minutes = remainingTimeInSeconds / 60
    val seconds = remainingTimeInSeconds % 60
    val infiniteTransition = rememberInfiniteTransition()
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Dialog(
        onDismissRequest = { /* Prevent dismissal by clicking outside */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = activity?.name ?: "Thời gian nghỉ ngơi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hãy thư giãn và tận hưởng khoảng thời gian này",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(200.dp),
                        strokeWidth = 12.dp,
                        color = activity?.color ?: PastelBlue,
                        trackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                    Box(
                        modifier = Modifier
                            .size(120.dp * breatheScale)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        (activity?.color ?: PastelBlue).copy(alpha = 0.7f),
                                        (activity?.color ?: PastelBlue).copy(alpha = 0.0f)
                                    )
                                )
                            )
                    )
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = when {
                        remainingTimeInSeconds > totalTimeInSeconds * 2 / 3 -> "Hít vào sâu..."
                        remainingTimeInSeconds > totalTimeInSeconds * 1 / 3 -> "Giữ hơi thở..."
                        else -> "Thở ra từ từ..."
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = activity?.color ?: PastelBlue,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Kết thúc sớm")
                }
            }
        }
    }
}