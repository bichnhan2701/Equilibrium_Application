package com.example.equilibrium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.equilibrium.R
import com.example.equilibrium.ui.theme.EquilibriumTheme

@Composable
fun Energy(onDismiss: () -> Unit) {
    var energy by remember { mutableFloatStateOf(20f) } // giá trị mặc định
    val (label, iconRes) = when (energy) {
        in 0f..20f -> "Kiệt sức" to R.drawable.energy_exhausted
        in 21f..40f -> "Mệt mỏi" to R.drawable.energy_tired
        in 41f..60f -> "Trung bình" to R.drawable.energy_average
        in 61f..80f -> "Tốt" to R.drawable.energy_good
        else -> "Rất tốt" to R.drawable.energy_great
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEBF9FC),
                            Color(0xFFAEDEE5),
                            Color(0xFF7CCFDA)
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.energy_nobg),
                        contentDescription = "Energy Icon",
                        tint = Color(0xFF047381),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Năng Lượng",
                        color = Color(0xFF047381),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        tint = Color(0xFF048ECC),
                        contentDescription = "Close"
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Bạn đang cảm thấy năng lượng thế nào?",
                textAlign = TextAlign.Center,
                color = Color(0xFF0C7CAF),
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontSize = 18.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(60.dp))

            // Slider energy
            Slider(
                value = energy,
                onValueChange = { energy = it },
                valueRange = 0f..100f,
                steps = 0, // 5 mức, chia 4 đoạn
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF32B4EF),
                    activeTrackColor = Color(0xFF32B4EF),
                    inactiveTrackColor = Color(0xFFFFFFFF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("${energy.toInt()}%", fontSize = 16.sp, color = Color(0xFF19A0DE))

            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .width(120.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E6B7B))
            ) {
                Text(
                    "OK",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEnergyScreen() {
    EquilibriumTheme {
        Energy(onDismiss = {})
    }
}
