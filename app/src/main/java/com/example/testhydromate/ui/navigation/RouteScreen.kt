package com.example.testhydromate.ui.navigation

import com.example.testhydromate.ui.screens.onboarding.InputPersonalScreen
import com.example.testhydromate.ui.screens.splash.SplashScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.screens.auth.LoginScreen
import com.example.testhydromate.ui.screens.home.HomeScreen
import com.example.testhydromate.ui.screens.onboarding.InputHabitScreen
import com.example.testhydromate.ui.screens.splash.SplashViewModel


@Composable
fun RouteScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ){

        // SPLASH SCREEN
        composable <Splash>(
            exitTransition = { fadeOut(tween(1000)) }
        ){
            val viewModel = hiltViewModel<SplashViewModel>()
            SplashScreen(
                onFinished = {
                    val destination = if (viewModel.isUserLoggedIn()) Home else Login

                    navController.navigate(destination) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        // LOGIN
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(PersonalDetails)
                }
            )
        }

        // INPUT PERSONAL DETAILS
        composable<PersonalDetails> {
            InputPersonalScreen(
                onContinueClicked = {
                    navController.navigate(HabitsInput)
                }
            )
        }

        // INPUT HABITS
        composable<HabitsInput> {
            InputHabitScreen(
                onContinueClicked = {
                    navController.navigate(WeatherInput)
                },
                onBackClicked = {
                    navController.popBackStack()
                }

            )
        }


        // HOME
        composable<Home> {
            HomeScreen(
                onLogout = {
                    // Kalau logout, kembali ke Login dan hapus history
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }

}
