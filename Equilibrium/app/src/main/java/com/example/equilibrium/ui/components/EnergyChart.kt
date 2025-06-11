package com.example.equilibrium.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equilibrium.R
import kotlin.math.min
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap


@Composable
fun EnergyStatus(
    energyPercent: Int = 91,
    statusText: String = "Trạng thái rất tốt!",
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .size(180.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Icon + Title + Refresh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.energy_nobg),
                        contentDescription = "Energy Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Năng lượng",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF497A8C),
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Làm mới",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Progress + % in center
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 10.dp.toPx()
                    val diameter = min(size.width, size.height)
                    val radius = diameter / 2
                    val topLeft = Offset(
                        (size.width - diameter) / 2,
                        (size.height - diameter) / 2
                    )
                    val arcRect = Size(diameter, diameter)

                    // Background circle (gray)
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcRect,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )

                    // Gradient sweep
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(Color(0xFFB0DAF5), Color(0xFF2D93C9))
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * (energyPercent / 100f),
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcRect,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Text(
                    text = "$energyPercent%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF497A8C)
                )
            }

            // Trạng thái
            Text(
                text = statusText,
                fontSize = 14.sp,
                color = Color(0xFF00C853),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
