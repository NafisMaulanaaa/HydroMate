@file:Suppress("UnusedMaterial3ScaffoldPaddingParameter")

package com.example.testhydromate.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testhydromate.ui.components.HydroBottomBar
import com.example.testhydromate.ui.screens.auth.LoginScreen
import com.example.testhydromate.ui.screens.history.HistoryScreen
import com.example.testhydromate.ui.screens.home.HomeScreen
import com.example.testhydromate.ui.screens.home.ResultScreen
import com.example.testhydromate.ui.screens.onboarding.*
import com.example.testhydromate.ui.screens.profile.MyProfile
import com.example.testhydromate.ui.screens.profile.NotificationsScreen
import com.example.testhydromate.ui.screens.profile.ProfileScreen
import com.example.testhydromate.ui.screens.splash.SplashScreen
import com.example.testhydromate.ui.screens.splash.SplashViewModel

@Composable
fun RouteScreen() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.HOME.route,
        Screen.HISTORY.route,
        Screen.PROFILE.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val selectedIndex = when (currentRoute) {
                    Screen.HOME.route -> 0
                    Screen.HISTORY.route -> 1
                    Screen.PROFILE.route -> 2
                    else -> 0
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HydroBottomBar(
                        selectedIndex = selectedIndex,
                        onHome = {
                            navController.navigate(Screen.HOME.route) {
                                launchSingleTop = true
                            }
                        },
                        onHistory = {
                            navController.navigate(Screen.HISTORY.route) {
                                launchSingleTop = true
                            }
                        },
                        onProfile = {
                            navController.navigate(Screen.PROFILE.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { _ ->   // 🔥 SENGAJA DI-IGNORE → floating bottom bar, NO WARNING

        NavHost(
            navController = navController,
            startDestination = Screen.SPLASH.route
        ) {

            // ===== SPLASH =====
            composable(Screen.SPLASH.route) {
                val vm = hiltViewModel<SplashViewModel>()
                SplashScreen(
                    onFinished = {
                        navController.navigate(
                            if (vm.isUserLoggedIn()) Screen.HOME.route else Screen.LOGIN.route
                        ) {
                            popUpTo(Screen.SPLASH.route) { inclusive = true }
                        }
                    }
                )
            }

            // ===== AUTH =====
            composable(Screen.LOGIN.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.HOME.route) {
                            popUpTo(Screen.LOGIN.route) { inclusive = true }
                        }
                    },
                    onRegisterSuccess = {
                        navController.navigate(Screen.INPUT_PERSONAL.route) {
                            popUpTo(Screen.LOGIN.route) { inclusive = true }
                        }
                    }
                )
            }

            // ===== ONBOARDING =====
            composable(Screen.INPUT_PERSONAL.route) {
                val vm = hiltViewModel<OnboardingViewModel>()
                InputPersonalScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.INPUT_HABIT.route)
                    }
                )
            }

            composable(Screen.INPUT_HABIT.route) {
                val vm = hiltViewModel<OnboardingViewModel>()
                InputHabitScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.INPUT_WEATHER.route)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable(Screen.INPUT_WEATHER.route) {
                val vm = hiltViewModel<OnboardingViewModel>()
                InputWeatherScreen(
                    viewModel = vm,
                    onContinueClicked = {
                        navController.navigate(Screen.LOADING.route)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable(Screen.LOADING.route) {
                val vm = hiltViewModel<OnboardingViewModel>()
                LoadingResultScreen(
                    viewModel = vm,
                    onFinished = {
                        vm.submitOnboarding()
                        navController.navigate(Screen.RESULT.route)
                    }
                )
            }

            composable(Screen.RESULT.route) {
                val vm = hiltViewModel<OnboardingViewModel>()
                ResultScreen(
                    viewModel = vm,
                    onContinueToHome = {
                        navController.navigate(Screen.HOME.route) {
                            popUpTo(Screen.RESULT.route) { inclusive = true }
                        }
                    }
                )
            }

            // ===== MAIN =====
            composable(Screen.HOME.route) { HomeScreen() }
            composable(Screen.HISTORY.route) { HistoryScreen() }

            composable(Screen.PROFILE.route) {
                ProfileScreen(
                    onNavigateToMyProfile = {
                        navController.navigate(Screen.MY_PROFILE.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.NOTIFICATIONS.route)
                    },
                    onLogoutSuccess = {
                        navController.navigate(Screen.LOGIN.route) {
                            popUpTo(0)
                        }
                    }
                )

            }

            composable(Screen.MY_PROFILE.route) {
                MyProfile(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.NOTIFICATIONS.route) {
                NotificationsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

        }
    }
}
