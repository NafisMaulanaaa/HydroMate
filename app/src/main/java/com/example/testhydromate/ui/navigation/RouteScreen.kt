package com.example.testhydromate.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.testhydromate.ui.screens.home.ResultScreen
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

    // Logika menampilkan BottomBar hanya di screen tertentu
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
            // --- SPLASH ---
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

            // --- LOGIN ---
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

            // --- ONBOARDING GRAPH (Disini perubahannya) ---
            navigation(route = Screen.ONBOARDING.route, startDestination = Screen.PERSONAL.route) {

                composable(Screen.PERSONAL.route) { entry ->
                    // 1. Ambil Entry milik Parent (Screen.ONBOARDING)
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(Screen.ONBOARDING.route)
                    }
                    // 2. Init ViewModel menggunakan scope parentEntry
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputPersonalScreen(
                        viewModel = vm,
                        onContinueClicked = { navController.navigate(Screen.HABIT.route) }
                    )
                }

                composable(Screen.HABIT.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(Screen.ONBOARDING.route)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputHabitScreen(
                        viewModel = vm,
                        onContinueClicked = { navController.navigate(Screen.WEATHER.route) },
                        onBackClicked = { navController.popBackStack() }
                    )
                }

                composable(Screen.WEATHER.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(Screen.ONBOARDING.route)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    InputWeatherScreen(
                        viewModel = vm,
                        onContinueClicked = { navController.navigate(Screen.LOADING.route) },
                        onBackClicked = { navController.popBackStack() }
                    )
                }

                composable(Screen.LOADING.route) { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(Screen.ONBOARDING.route)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    LoadingResultScreen(
                        viewModel = vm,
                        onFinished = {
                            navController.navigate(Screen.RESULT.route) {
                                // Hapus loading biar gak bisa diback
                                popUpTo(Screen.LOADING.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.RESULT.route) { entry ->
                    // Di sini kita masih butuh VM untuk nampilin data hasil kalkulasi
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry(Screen.ONBOARDING.route)
                    }
                    val vm = hiltViewModel<OnboardingViewModel>(parentEntry)

                    ResultScreen(
                        // Asumsi MainDailyGoalContainer menerima viewModel atau state dari VM
                        viewModel = vm,
                        onContinueToHome = {
                            navController.navigate(Screen.HOME.route) {
                                // Hapus seluruh graph onboarding dari backstack
                                popUpTo(Screen.ONBOARDING.route) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // --- MAIN APP ---
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