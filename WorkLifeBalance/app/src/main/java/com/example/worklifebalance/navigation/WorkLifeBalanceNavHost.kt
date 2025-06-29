package com.example.worklifebalance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.worklifebalance.ui.screen.AnalyticsReport
import com.example.worklifebalance.ui.screen.Dashboard
import com.example.worklifebalance.ui.screen.DomainManagement
import com.example.worklifebalance.ui.screen.GoalDetailManagement
import com.example.worklifebalance.ui.screen.GoalManagement
import com.example.worklifebalance.ui.screen.Login
import com.example.worklifebalance.ui.screen.RestSuggestion
import com.example.worklifebalance.ui.screen.Splash
import com.example.worklifebalance.ui.screen.TaskManagement
import com.example.worklifebalance.ui.screen.UserProfile

@Composable
fun WorkLifeBalanceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            Splash(navController)
        }
        composable (Screen.Onboarding.route) {
//            Onboarding(context, navController)
        }
        composable(Screen.Dashboard.route) { Dashboard(navController) }
        composable(Screen.GoalManagement.route) { GoalManagement(navController) }
        composable(Screen.AnalyticsReports.route) {
            AnalyticsReport()
        }
        composable(Screen.Rest.route) {
            RestSuggestion(navController)
        }
        composable(Screen.GoalDetailManagement.route) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId") ?: ""
            GoalDetailManagement(navController, goalId = goalId)
        }
        composable(Screen.TaskManagement.route) { TaskManagement() }
        composable(Screen.UserProfile.route) { UserProfile(navController = navController) }
        composable(Screen.Login.route) { Login(navController = navController) }
        composable(Screen.DomainManagement.route) {
            DomainManagement(navController)
        }
    }
}