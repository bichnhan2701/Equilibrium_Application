package com.example.worklifebalance.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worklifebalance.R
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth
import com.example.worklifebalance.navigation.Screen

@Composable
fun Splash(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1500)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Nền là một ảnh
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Nội dung chữ ở giữa màn hình
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // WORK-LIFE
            Text(
                text = "WORK-LIFE",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF7C68B6), // tím nhạt
                letterSpacing = 6.sp,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(2.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7C68B6), // B
                                Color(0xFF8B8ADE), // A
                                Color(0xFFA68CD9), // L
                                Color(0xFFB3B8F5), // A
                                Color(0xFFF5C8D1), // N
                                Color(0xFFF8E16C), // C
                                Color(0xFFC1E5AE)  // E
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.height(4.dp))
            // BALANCE với hiệu ứng màu cầu vồng
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF7C68B6),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("B")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF8B8ADE),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("A")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFA68CD9),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("L")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFB3B8F5),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("A")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFF5C8D1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("N")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFF8E16C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("C")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFC1E5AE),
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp
                        )
                    ) {
                        append("E")
                    }
                },
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(2.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7C68B6), // B
                                Color(0xFF8B8ADE), // A
                                Color(0xFFA68CD9), // L
                                Color(0xFFB3B8F5), // A
                                Color(0xFFF5C8D1), // N
                                Color(0xFFF8E16C), // C
                                Color(0xFFC1E5AE)  // E
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(2.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7C68B6), // B
                                Color(0xFF8B8ADE), // A
                                Color(0xFFA68CD9), // L
                                Color(0xFFB3B8F5), // A
                                Color(0xFFF5C8D1), // N
                                Color(0xFFF8E16C), // C
                                Color(0xFFC1E5AE)  // E
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Cân bằng năng lượng - Theo đuổi mục tiêu",
                fontSize = 14.sp,
                color = Color(0xFF3A3A3A),
                textAlign = TextAlign.Center
            )
        }
    }
}
