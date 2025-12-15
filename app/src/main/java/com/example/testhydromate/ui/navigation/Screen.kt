sealed class Screen(val route: String) {
    object SPLASH : Screen("splash")
    object LOGIN : Screen("login")
    object ONBOARDING : Screen("onboarding")
    object PERSONAL : Screen("personal")
    object HABIT : Screen("habit")
    object WEATHER : Screen("weather")
    object LOADING : Screen("loading")
    object RESULT : Screen("result")
    object HOME : Screen("home")
    object PROFILE : Screen("profile")
    object HISTORY : Screen("history")
    object MY_PROFILE : Screen("my_profile")

}
