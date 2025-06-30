package com.example.worklifebalance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.worklifebalance.navigation.Screen
import com.example.worklifebalance.navigation.WorkLifeBalanceNavHost
import com.example.worklifebalance.ui.component.common.BottomNavBar
import com.example.worklifebalance.ui.theme.WorkLifeBalanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkLifeBalanceTheme {
                WorkLifeBalanceApp()
            }
        }
    }
}

@Composable
fun WorkLifeBalanceApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Splash.route
                && currentRoute != Screen.Onboarding.route
                && currentRoute != Screen.Login.route
            ) {
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            WorkLifeBalanceNavHost(navController = navController)
        }
    }
}