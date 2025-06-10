package com.example.equilibrium.navigation

sealed class Screens(val route: String) {
    data object Splash : Screens("splash")
    data object Onboard : Screens("onboard")
    data object Home : Screens("home")
    data object Goals : Screens("goals")
    data object Schedule : Screens("schedule")
    data object Chart : Screens("chart")
    data object User : Screens("user")
    data object Energy : Screens("energy")
}