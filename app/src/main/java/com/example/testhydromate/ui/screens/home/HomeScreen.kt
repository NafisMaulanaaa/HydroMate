package com.example.testhydromate.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onNavigateToAchievement: () -> Unit = {},
    onNavigateToStreak: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val total by viewModel.totalDrink.collectAsState()
    val target by viewModel.dailyTarget.collectAsState()
    val history by viewModel.history.collectAsState()
    val shouldShowAchievement by viewModel.shouldShowAchievement.collectAsState()

    var showAdjustSheet by remember { mutableStateOf(false) }

    // State untuk streak
    val streakCount by viewModel.streakCount.collectAsState()
    val isStreakActive by viewModel.isStreakActiveToday.collectAsState()

    // --- STATE UNTUK NOTIFIKASI DEWASA (MATURE NOTIFICATION) ---
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }

    LaunchedEffect(shouldShowAchievement) {
        if (shouldShowAchievement) {
            viewModel.resetAchievementFlag()
            onNavigateToAchievement()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header & Progress Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(74.dp))
                Text(
                    text = "Home",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.height(40.dp))
                WaterProgress(current = total, target = target)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    DrinkButton(
                        modifier = Modifier.width(200.dp),
                        text = "Drink ${viewModel.selectedAmount} mL",
                        onClick = { viewModel.drinkUsingSelectedAmount() }
                    )
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        IconButton(onClick = { showAdjustSheet = true }) {
                            Icon(Icons.Outlined.Edit, null, tint = PrimaryBlue)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // AREA HISTORY BOX
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                TodayHistoryCard(
                    history = history,
                    onUpdate = { log ->
                        // 1. Update data via ViewModel
                        viewModel.updateLog(log)

                        // 2. Tampilkan Notifikasi Custom untuk EDIT
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                        notificationMessage = "Drink record at $time updated to ${log.amount} mL"
                        showNotification = true
                    },
                    onDeleteConfirm = { log ->
                        // 1. Hapus data via ViewModel
                        viewModel.deleteLog(log)

                        // 2. Tampilkan Notifikasi Custom untuk DELETE
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                        notificationMessage = "Drink record at $time has been deleted"
                        showNotification = true
                    }
                )
            }
        }

        // STREAK (Floating Trophy)
        FloatingStreakTrophy(
            streakCount = streakCount,
            isActive = isStreakActive,
            onClick = onNavigateToStreak
        )

        // --- CUSTOM NOTIFICATION (HIJAU DI ATAS) ---
        AnimatedVisibility(
            visible = showNotification,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp) // Jarak dari atas layar agar tidak tertutup notch
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color(0xFF4CAF50), // Warna Hijau Mature
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = notificationMessage,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Auto-hide notifikasi setelah 3 detik
            LaunchedEffect(showNotification) {
                if (showNotification) {
                    delay(3000)
                    showNotification = false
                }
            }
        }

        // Bottom Sheet Adjust Amount
        if (showAdjustSheet) {
            AdjustDrinkAmountBottomSheet(
                initialAmount = viewModel.selectedAmount,
                onDismiss = { showAdjustSheet = false },
                onSave = { newAmount ->
                    viewModel.updateSelectedAmount(newAmount)
                    showAdjustSheet = false
                }
            )
        }
    }
}
