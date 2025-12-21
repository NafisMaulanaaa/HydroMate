package com.example.testhydromate.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.components.*
import com.example.testhydromate.ui.screens.history.ConfirmDeleteDialog
import com.example.testhydromate.ui.screens.history.showDeleteToast
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onNavigateToAchievement: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val total by viewModel.totalDrink.collectAsState()
    val target by viewModel.dailyTarget.collectAsState()
    val history by viewModel.history.collectAsState()
    val shouldShowAchievement by viewModel.shouldShowAchievement.collectAsState()

    var showAdjustSheet by remember { mutableStateOf(false) }

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

            // AREA HISTORY BOX (Outline tidak mepet)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp) // PENTING: Supaya outline box ngga mepet layar
            ) {
                TodayHistoryCard(
                    history = history,
                    onUpdate = { viewModel.updateLog(it) },
                    onDeleteConfirm = { viewModel.deleteLog(it) }
                )
            }
        }
    }
}