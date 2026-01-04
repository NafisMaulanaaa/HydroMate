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

@OptIn(ExperimentalMaterial3Api::class) // Tambahkan OptIn
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

    // State untuk Bottom Sheets
    var showAdjustDrinkSheet by remember { mutableStateOf(false) } // Sheet atur jumlah minum
    var showEditGoalSheet by remember { mutableStateOf(false) }   // Sheet atur target harian (BARU)

    // State untuk streak
    val streakCount by viewModel.streakCount.collectAsState()
    val isStreakActive by viewModel.isStreakActiveToday.collectAsState()

    // State Notifikasi
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }

    // Temp state untuk edit goal
    var tempGoalValue by remember { mutableStateOf("") }

    // Sync tempGoal saat target berubah atau sheet dibuka
    LaunchedEffect(target) {
        tempGoalValue = target.toString()
    }

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

                // --- UPDATE COMPONENT ---
                WaterProgress(
                    current = total,
                    target = target,
                    onTargetClick = {
                        tempGoalValue = target.toString()
                        showEditGoalSheet = true // Buka sheet edit goal
                    }
                )

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
                        // Tombol ini membuka sheet atur jumlah minum (AdjustDrinkAmountBottomSheet)
                        IconButton(onClick = { showAdjustDrinkSheet = true }) {
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
                        viewModel.updateLog(log)
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                        notificationMessage = "Drink record at $time updated to ${log.amount} mL"
                        showNotification = true
                    },
                    onDeleteConfirm = { log ->
                        viewModel.deleteLog(log)
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                        notificationMessage = "Drink record at $time has been deleted"
                        showNotification = true
                    }
                )
            }
        }

        // STREAK
        FloatingStreakTrophy(
            streakCount = streakCount,
            isActive = isStreakActive,
            onClick = onNavigateToStreak
        )

        // NOTIFIKASI
        AnimatedVisibility(
            visible = showNotification,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                color = Color(0xFF4CAF50),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 8.dp
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(notificationMessage, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            LaunchedEffect(showNotification) {
                if (showNotification) {
                    delay(3000)
                    showNotification = false
                }
            }
        }

        // --- BOTTOM SHEET 1: ADJUST DRINK AMOUNT (Gelas minum) ---
        if (showAdjustDrinkSheet) {
            AdjustDrinkAmountBottomSheet(
                initialAmount = viewModel.selectedAmount,
                onDismiss = { showAdjustDrinkSheet = false },
                onSave = { newAmount ->
                    viewModel.updateSelectedAmount(newAmount)
                    showAdjustDrinkSheet = false
                }
            )
        }

        // --- BOTTOM SHEET 2: EDIT GOAL (Target Harian) ---
        if (showEditGoalSheet) {
            ModalBottomSheet(
                onDismissRequest = { showEditGoalSheet = false },
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                // Re-use komponen EditGoalSheetContent dari ui.components
                EditGoalSheetContent(
                    tempValue = tempGoalValue,
                    onValueChange = { tempGoalValue = it },
                    onCancel = { showEditGoalSheet = false },
                    onSave = {
                        val newGoal = tempGoalValue.toIntOrNull()
                        if (newGoal != null && newGoal > 0) {
                            viewModel.updateDailyGoal(newGoal) // Panggil ViewModel
                            showEditGoalSheet = false

                            // Optional: Tampilkan notifikasi
                            notificationMessage = "Daily goal updated to $newGoal mL"
                            showNotification = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}