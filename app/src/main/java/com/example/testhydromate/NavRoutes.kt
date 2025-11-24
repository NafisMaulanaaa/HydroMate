package com.example.testhydromate

sealed class Screen(val route: String) {
    object Splash : Screen("Splash")
    object Login : Screen("Login")
}
