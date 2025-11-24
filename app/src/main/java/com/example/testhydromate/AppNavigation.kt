import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.testhydromate.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // SPLASH SCREEN
        composable(
            route = Screen.Splash.route,
            exitTransition = { fadeOut(tween(1000)) }
        ) {
            SpalshScreen(
                onFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // LOGIN SCREEN
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn(tween(2000)) }
        ) {
            LoginScreen()
        }
    }
}
