package com.example.equilibrium.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.equilibrium.domain.repository.PreferencesRepository
import com.example.equilibrium.ui.screens.Chart
import com.example.equilibrium.ui.screens.Energy
import com.example.equilibrium.ui.screens.Splash
import com.example.equilibrium.ui.screens.HomeScreen
import com.example.equilibrium.ui.screens.Onboard
import com.example.equilibrium.ui.screens.Schedule
import com.example.equilibrium.ui.screens.User


@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val preferences = remember { PreferencesRepository(context) }

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        // Splash
        composable(Screens.Splash.route) {
            Splash(navController = navController, preferences = preferences)
        }

        // Onboarding
        composable(Screens.Onboard.route) {
            Onboard(
                onSkip = {
                    preferences.setSeenOnboarding()
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Onboard.route) { inclusive = true }
                    }
                },
                onDone = {
                    preferences.setSeenOnboarding()
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Onboard.route) { inclusive = true }
                    }
                }
            )
        }

        // Bottom navigation tabs
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.Goals.route) {
            // TODO: Replace with actual GoalsScreen()
        }
        composable(Screens.Schedule.route) {
            Schedule(navController = navController)
        }
        composable(Screens.Chart.route) {
            Chart(navController = navController)
        }
        composable(Screens.User.route) {
            User(navController = navController)
        }

        // Energy screen (popup)
        composable(Screens.Energy.route) {
            Energy(onDismiss = {
                navController.popBackStack()
            })
        }
    }
}

