package com.example.testhydromate.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testhydromate.ui.components.HydroBottomBar
import com.example.testhydromate.ui.screens.auth.LoginScreen
import com.example.testhydromate.ui.screens.history.HistoryScreen
import com.example.testhydromate.ui.screens.home.HomeScreen
import com.example.testhydromate.ui.screens.home.MainDailyGoalContainer
import com.example.testhydromate.ui.screens.onboarding.*
import com.example.testhydromate.ui.screens.profile.ProfileScreen
import com.example.testhydromate.ui.screens.profile.MyProfile
import com.example.testhydromate.ui.screens.splash.SplashScreen
import com.example.testhydromate.ui.screens.splash.SplashViewModel

@Composable
fun RouteScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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

                HydroBottomBar(
                    selectedIndex = selectedIndex,
                    onHome = {
                        if (currentRoute != Screen.HOME.route) {
                            navController.navigate(Screen.HOME.route) {
                                popUpTo(Screen.HOME.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onHistory = {
                        if (currentRoute != Screen.HISTORY.route) {
                            navController.navigate(Screen.HISTORY.route) {
                                popUpTo(Screen.HOME.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onProfile = {
                        if (currentRoute != Screen.PROFILE.route) {
                            navController.navigate(Screen.PROFILE.route) {
                                popUpTo(Screen.HOME.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.SPLASH.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.SPLASH.route) {
                val vm = hiltViewModel<SplashViewModel>()
                SplashScreen(
                    onFinished = {
                        val dest = if (vm.isUserLoggedIn()) Screen.HOME.route else Screen.LOGIN.route
                        navController.navigate(dest) {
                            popUpTo(Screen.SPLASH.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.LOGIN.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.HOME.route) {
                            popUpTo(Screen.LOGIN.route) { inclusive = true }
                        }
                    },
                    onRegisterSuccess = {
                        navController.navigate(Screen.ONBOARDING.route)
                    }
                )
            }

            navigation(route = Screen.ONBOARDING.route, startDestination = Screen.PERSONAL.route) {
                composable(Screen.PERSONAL.route) {
                    val vm = hiltViewModel<OnboardingViewModel>()
                    InputPersonalScreen(vm) { navController.navigate(Screen.HABIT.route) }
                }
                composable(Screen.HABIT.route) {
                    val vm = hiltViewModel<OnboardingViewModel>()
                    InputHabitScreen(vm, { navController.navigate(Screen.WEATHER.route) }, { navController.popBackStack() })
                }
                composable(Screen.WEATHER.route) {
                    val vm = hiltViewModel<OnboardingViewModel>()
                    InputWeatherScreen(vm, { navController.navigate(Screen.LOADING.route) }, { navController.popBackStack() })
                }
                composable(Screen.LOADING.route) {
                    val vm = hiltViewModel<OnboardingViewModel>()
                    LoadingResultScreen(vm) { navController.navigate(Screen.RESULT.route) }
                }
                composable(Screen.RESULT.route) {
                    MainDailyGoalContainer {
                        navController.navigate(Screen.HOME.route) { popUpTo(Screen.ONBOARDING.route) { inclusive = true } }
                    }
                }
            }

            composable(Screen.HOME.route) {
                HomeScreen(
                    onLogout = {
                        navController.navigate(Screen.LOGIN.route) {
                            popUpTo(Screen.HOME.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.PROFILE.route) {
                ProfileScreen(
                    onLogoutSuccess = {
                        navController.navigate(Screen.LOGIN.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToMyProfile = {
                        navController.navigate(Screen.MY_PROFILE.route)
                    }
                )
            }

            // Tambahkan route untuk My Profile
            composable(Screen.MY_PROFILE.route) {
                MyProfile(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.HISTORY.route) {
                HistoryScreen()
            }
        }
    }
}