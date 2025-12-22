@file:Suppress("UnusedMaterial3ScaffoldPaddingParameter")

package com.example.testhydromate.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // Tambahan penting
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation // Tambahan penting
import androidx.navigation.compose.rememberNavController
import com.example.testhydromate.ui.components.HydroBottomBar
import com.example.testhydromate.ui.screens.achievement.AchievementScreen
import com.example.testhydromate.ui.screens.auth.LoginScreen
import com.example.testhydromate.ui.screens.history.HistoryScreen
import com.example.testhydromate.ui.screens.home.HomeScreen
import com.example.testhydromate.ui.screens.home.ResultScreen
import com.example.testhydromate.ui.screens.onboarding.*
import com.example.testhydromate.ui.screens.profile.*
import com.example.testhydromate.ui.screens.splash.SplashScreen
import com.example.testhydromate.ui.screens.splash.SplashViewModel

// Definisikan nama route untuk Graph Onboarding agar konsisten
const val ONBOARDING_GRAPH_ROUTE = "onboarding_graph"

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
                                popUpTo(Screen.HOME.route) { saveState = true }
                                restoreState = true
                            }
                        },
                        onHistory = {
                            navController.navigate(Screen.HISTORY.route) {
                                launchSingleTop = true
                                popUpTo(Screen.HOME.route) { saveState = true }
                                restoreState = true
                            }
                        },
                        onProfile = {
                            navController.navigate(Screen.PROFILE.route) {
                                launchSingleTop = true
                                popUpTo(Screen.HOME.route) { saveState = true }
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { _ ->

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
                        // Perubahan 1: Arahkan ke Graph Route, bukan screen pertama
                        navController.navigate(ONBOARDING_GRAPH_ROUTE) {
                            popUpTo(Screen.LOGIN.route) { inclusive = true }
                        }
                    }
                )
            }

            // ===== ONBOARDING GRAPH (SHARED VIEWMODEL) =====
            navigation(
                startDestination = Screen.INPUT_PERSONAL.route,
                route = ONBOARDING_GRAPH_ROUTE
            ) {

                // 1. INPUT PERSONAL
                composable(Screen.INPUT_PERSONAL.route) { entry ->
                    // Ambil ViewModel dari Parent Entry (Graph)
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(ONBOARDING_GRAPH_ROUTE)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputPersonalScreen(
                        viewModel = vm,
                        onContinueClicked = {
                            navController.navigate(Screen.INPUT_HABIT.route)
                        }
                    )
                }

                // 2. INPUT HABIT
                composable(Screen.INPUT_HABIT.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(ONBOARDING_GRAPH_ROUTE)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputHabitScreen(
                        viewModel = vm,
                        onContinueClicked = {
                            navController.navigate(Screen.INPUT_WEATHER.route)
                        },
                        onBackClicked = { navController.popBackStack() }
                    )
                }

                // 3. INPUT WEATHER
                composable(Screen.INPUT_WEATHER.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(ONBOARDING_GRAPH_ROUTE)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputWeatherScreen(
                        viewModel = vm,
                        onContinueClicked = {
                            navController.navigate(Screen.LOADING.route)
                        },
                        onBackClicked = { navController.popBackStack() }
                    )
                }

                // 4. LOADING
                composable(Screen.LOADING.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(ONBOARDING_GRAPH_ROUTE)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    LoadingResultScreen(
                        viewModel = vm,
                        onFinished = {
                            vm.submitOnboarding() // Submit data ke backend/local
                            navController.navigate(Screen.RESULT.route) {
                                // Hapus loading dari backstack agar user tidak bisa back ke loading
                                popUpTo(Screen.LOADING.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 5. RESULT SCREEN
                composable(Screen.RESULT.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(ONBOARDING_GRAPH_ROUTE)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    ResultScreen(
                        viewModel = vm, // VM masih membawa data hasil kalkulasi
                        onContinueToHome = {
                            navController.navigate(Screen.HOME.route) {
                                // Hapus SELURUH Graph Onboarding dari memori
                                popUpTo(ONBOARDING_GRAPH_ROUTE) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // ===== MAIN =====
            composable(Screen.HOME.route) {
                HomeScreen(
                    onNavigateToAchievement = {
                        navController.navigate(Screen.ACHIEVEMENT.route)
                    }
                )
            }

            composable(Screen.HISTORY.route) { HistoryScreen() }

            composable(Screen.PROFILE.route) {
                ProfileScreen(
                    onNavigateToMyProfile = {
                        navController.navigate(Screen.MY_PROFILE.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.NOTIFICATIONS.route)
                    },
                    onNavigateToAboutApp = {
                        navController.navigate(Screen.ABOUT_APP.route)
                    },
                    onLogoutSuccess = {
                        navController.navigate(Screen.LOGIN.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.MY_PROFILE.route) {
                MyProfile(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.NOTIFICATIONS.route) {
                NotificationsScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.ABOUT_APP.route) {
                AboutApp(
                    onBackClick = { navController.popBackStack() },
                    onPrivacyPolicyClick = {
                        navController.navigate(Screen.PRIVACY_POLICY.route)
                    }
                )
            }

            composable(Screen.PRIVACY_POLICY.route) {
                PrivacyPolicy(onBackClick = { navController.popBackStack() })
            }

            // ===== ACHIEVEMENT =====
            composable(Screen.ACHIEVEMENT.route) {
                AchievementScreen(
                    onBackToHome = {
                        navController.navigate(Screen.HOME.route) {
                            popUpTo(Screen.ACHIEVEMENT.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}