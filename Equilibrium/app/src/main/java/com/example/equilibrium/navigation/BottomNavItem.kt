package com.example.equilibrium.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    object Home : BottomNavItem("Trang chủ", Icons.Default.Home, Screens.Home.route)
    //    object Goals : BottomNavItem("Mục tiêu", Icons.Default.TrackChanges, Screens.Goals.route)
    object Schedule : BottomNavItem("Lịch biểu", Icons.Default.DateRange, Screens.Schedule.route)
    object Chart : BottomNavItem("Biểu đồ", Icons.Default.BarChart, Screens.Chart.route)
    object Profile : BottomNavItem("Cài đặt người dùng", Icons.Default.Person, Screens.User.route)
}