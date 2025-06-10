package com.example.equilibrium.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.equilibrium.navigation.BottomNavItem


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Schedule,
        BottomNavItem.Chart,
        BottomNavItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .height(74.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                clip = false
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        // BottomNavBar Background
        Surface(
            color = Color.White,
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2 icons trái
                items.take(2).forEach { item ->
                    val selected = currentRoute == item.route
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (selected) Color(0x1A555BFF) else Color.Transparent, // Màu nền mờ khi chọn
                                shape = CircleShape
                            )
                            .clickable {
                                if (!selected) {
                                    navController.navigate(item.route) {
                                        popUpTo(items.first().route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            tint = if (selected) Color(0xFF555BFF) else Color(0xFF181818)
                        )
                        // Vòng tròn nhỏ bên dưới nếu được chọn
                        if (selected) {
                            Box(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .offset(y = 8.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF555BFF), shape = CircleShape)
                            )
                        }
                    }
                }

                // Chỗ trống giữa cho nút Add
                Spacer(modifier = Modifier.width(48.dp))

                // 2 icons phải
                items.drop(2).forEach { item ->
                    val selected = currentRoute == item.route
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (selected) Color(0x1A555BFF) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                if (!selected) {
                                    navController.navigate(item.route) {
                                        popUpTo(items.first().route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            tint = if (selected) Color(0xFF555BFF) else Color(0xFF181818)
                        )
                        if (selected) {
                            Box(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .offset(y = 8.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF555BFF), shape = CircleShape)
                            )
                        }
                    }
                }
            }
        }

        // Floating Add Button (center)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-32).dp)
                .size(64.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF8ECDF6), Color(0xFFB7A7F7)) // gradient nhẹ xanh-tím
                    ),
                    shape = CircleShape
                )
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

