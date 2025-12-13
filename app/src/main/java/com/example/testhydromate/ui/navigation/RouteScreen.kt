package com.example.testhydromate.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

import com.example.testhydromate.ui.screens.splash.*
import com.example.testhydromate.ui.screens.auth.*
import com.example.testhydromate.ui.screens.home.*
import com.example.testhydromate.ui.screens.onboarding.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RouteScreen() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SPLASH
    ) {

        //Spalsh
        composable(
            route = Screen.SPLASH,
            exitTransition = {
                fadeOut(animationSpec = tween(600))
            }
        ) {
            val vm = hiltViewModel<SplashViewModel>()

            SplashScreen(
                onFinished = {
                    val dest =
                        if (vm.isUserLoggedIn()) Screen.HOME else Screen.LOGIN

                    navController.navigate(dest) {
                        popUpTo(Screen.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        //Login
        composable(
            route = Screen.LOGIN,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                )
            }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.HOME) {
                        popUpTo(Screen.LOGIN) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.ONBOARDING)
                }
            )
        }

        //Onboarding
        navigation(
            route = Screen.ONBOARDING,
            startDestination = Screen.PERSONAL
        ) {

            composable(
                route = Screen.PERSONAL,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { entry ->
                val parent = remember(entry) {
                    navController.getBackStackEntry(Screen.ONBOARDING)
                }
                val vm = hiltViewModel<OnboardingViewModel>(parent)

                InputPersonalScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.HABIT)
                    }
                )
            }

            composable(
                route = Screen.HABIT,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { entry ->
                val parent = remember(entry) {
                    navController.getBackStackEntry(Screen.ONBOARDING)
                }
                val vm = hiltViewModel<OnboardingViewModel>(parent)

                InputHabitScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.WEATHER)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.WEATHER,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { entry ->
                val parent = remember(entry) {
                    navController.getBackStackEntry(Screen.ONBOARDING)
                }
                val vm = hiltViewModel<OnboardingViewModel>(parent)

                InputWeatherScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.LOADING)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.LOADING,
                enterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                }
            ) { entry ->
                val parent = remember(entry) {
                    navController.getBackStackEntry(Screen.ONBOARDING)
                }
                val vm = hiltViewModel<OnboardingViewModel>(parent)

                LoadingResultScreen(
                    viewModel = vm,
                    onFinished = {
                        navController.navigate(Screen.RESULT)
                    }
                )
            }

            composable(
                route = Screen.RESULT,
                enterTransition = {
                    scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300))
                }
            ) {
                MainDailyGoalContainer(
                    onContinueToHome = {
                        navController.navigate(Screen.HOME) {
                            popUpTo(Screen.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }
        }

        //Home
        composable(
            route = Screen.HOME,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                )
            }
        ) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
