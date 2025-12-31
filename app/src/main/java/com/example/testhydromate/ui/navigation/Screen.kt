package com.example.testhydromate.ui.navigation

sealed class Screen(val route: String) {
    object SPLASH : Screen("splash")
    object LOGIN : Screen("login")

    // onboarding
    object INPUT_PERSONAL : Screen("input_personal")
    object INPUT_HABIT : Screen("input_habit")
    object INPUT_WEATHER : Screen("input_weather")
    object LOADING : Screen("loading")
    object RESULT : Screen("result")

    // main
    object HOME : Screen("home")
    object HISTORY : Screen("history")
    object PROFILE : Screen("profile")
    object MY_PROFILE : Screen("my_profile")
    object NOTIFICATIONS : Screen("notifications")
    object ABOUT_APP : Screen("about_app")
    object PRIVACY_POLICY : Screen("privacy_policy")

    //achievement
    object ACHIEVEMENT : Screen("achievement")

    // streak
    object STREAK : Screen("streak")
}

//data class Route(val route: String)