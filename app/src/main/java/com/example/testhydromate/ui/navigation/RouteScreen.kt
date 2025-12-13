package com.example.testhydromate.ui.navigation

import com.example.testhydromate.ui.screens.onboarding.InputPersonalScreen
import com.example.testhydromate.ui.screens.splash.SplashScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.navigation
import com.example.testhydromate.ui.screens.auth.LoginScreen
import com.example.testhydromate.ui.screens.home.DailyGoalMainScreen
import com.example.testhydromate.ui.screens.home.HomeScreen
import com.example.testhydromate.ui.screens.home.MainDailyGoalContainer
import com.example.testhydromate.ui.screens.onboarding.InputHabitScreen
import com.example.testhydromate.ui.screens.onboarding.InputWeatherScreen
import com.example.testhydromate.ui.screens.onboarding.LoadingResultScreen
import com.example.testhydromate.ui.screens.onboarding.OnboardingViewModel
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
        navigation<OnboardingGraph>(startDestination = PersonalDetails) {

            composable<PersonalDetails> { entry ->

                val parentEntry = remember(entry) { navController.getBackStackEntry(OnboardingGraph) }
                val viewModel = hiltViewModel<OnboardingViewModel>(parentEntry)

                InputPersonalScreen(
                    viewModel = viewModel,
                    onContinueClicked = { navController.navigate(HabitsInput) }
                )
            }

            composable<HabitsInput> { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry(OnboardingGraph) }
                val viewModel = hiltViewModel<OnboardingViewModel>(parentEntry)

                InputHabitScreen(
                    viewModel = viewModel,
                    onContinueClicked = { navController.navigate(WeatherInput) },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<WeatherInput> { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(OnboardingGraph)
                }
                val viewModel = hiltViewModel<OnboardingViewModel>(parentEntry)

                InputWeatherScreen(
                    viewModel = viewModel,
                    onContinueClicked = {
                        navController.navigate(LoadingResult)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }

            //Laoding Screen
            composable<LoadingResult> { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(OnboardingGraph)
                }
                val viewModel = hiltViewModel<OnboardingViewModel>(parentEntry)

                LoadingResultScreen(
                    viewModel = viewModel,
                    onFinished = {
                        navController.navigate(ResultGoal)
                    }
                )
            }

            //Result Screen
            composable<ResultGoal> {
                MainDailyGoalContainer(
                    onContinueToHome = {
                        navController.navigate(Home) {
                            popUpTo<OnboardingGraph> { inclusive = true }
                        }
                    }
                )
            }

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
