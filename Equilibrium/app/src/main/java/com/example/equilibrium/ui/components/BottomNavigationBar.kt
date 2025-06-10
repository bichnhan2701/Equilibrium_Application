package com.example.equilibrium.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.equilibrium.navigation.Screens
import com.example.equilibrium.navigation.BottomNavItem


@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
//        BottomNavItem.Goals,
        BottomNavItem.Schedule,
        BottomNavItem.Chart,
        BottomNavItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    Surface(
        color = Color.Transparent,
        tonalElevation = 10.dp,
        shape = shape,
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = shape,
                clip = false
            )
            .clip(shape) // Clip đúng theo shape
            .border(
                width = 1.dp,
                color = Color.White,
                shape = shape
            )
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color(0xFFF4F4F4),
            tonalElevation = 5.dp
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screens.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    },
                    label = null,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFE4E5EA)
                    ),
                    alwaysShowLabel = false // Không hiện label
                )
            }
        }
    }
}
