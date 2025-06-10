package com.example.equilibrium.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.equilibrium.ui.components.BottomNavigationBar
import com.example.equilibrium.ui.theme.EquilibriumTheme
import androidx.compose.foundation.layout.padding
import com.example.equilibrium.ui.components.BottomNavBar


@Composable
fun Home(navController: NavController) {
    Scaffold(
        bottomBar = {
//            BottomNavigationBar(navController = navController)
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    EquilibriumTheme {
        Home(navController = navController)
    }
}