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
    viewModel: HomeViewModel = hiltViewModel()
) {
    val total by viewModel.totalDrink.collectAsState()
    val history by viewModel.history.collectAsState()
    val target by viewModel.dailyTarget.collectAsState()
    val context = LocalContext.current

    var selectedDelete by remember { mutableStateOf<WaterLog?>(null) }
    var showAdjustSheet by remember { mutableStateOf(false) }

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

        selectedDelete?.let { log ->
            val time = remember(log.timestamp) {
                java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(Date(log.timestamp))
            }

            ConfirmDeleteDialog(
                time = time,
                onCancel = { selectedDelete = null },
                onConfirm = {
                    viewModel.deleteLog(log)
                    showDeleteToast(context, time)
                    selectedDelete = null
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(62.dp))

            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            Spacer(modifier = Modifier.height(40.dp))

            WaterProgress(
                current = total,
                target = target
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

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = { showAdjustSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit drink amount",
                            tint = PrimaryBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Gunakan weight agar responsif di berbagai layar
            ) {
                TodayHistoryCard(
                    history = history,
                    onUpdate = { viewModel.updateLog(it) },
                    onDeleteConfirm = { selectedDelete = it }
                )
            }
        }
    }
}