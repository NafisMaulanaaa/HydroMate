package com.example.testhydromate.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.*

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val total by viewModel.totalDrink.collectAsState()
    val history by viewModel.history.collectAsState()
    val target by viewModel.dailyTarget.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. Spacer Atas ---
            Spacer(modifier = Modifier.height(62.dp))

            // ===== TITLE =====
            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            // --- 2. Spacer Tengah ---
            Spacer(modifier = Modifier.height(64.dp))

            WaterProgress(
                current = total,
                target = target
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ===== DRINK BUTTON =====
            DrinkButton(
                text = "Drink 100 mL",
                onClick = { viewModel.drink(100) },
            )

            // Spacer pendorong ke bawah
            Spacer(modifier = Modifier.height(20.dp))

            // ===== TODAY HISTORY =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp) // Berikan tinggi fix atau minHeight agar terlihat "card" panjang
            ) {
                TodayHistoryCard(
                    history = history,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
