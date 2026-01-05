package com.example.testhydromate.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.ChartCardContainer
import com.example.testhydromate.ui.components.DateNavigator
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TimeFilterSection
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReportScreen(
    onBackClick: () -> Unit = {},
    viewModel: ReportViewModel = hiltViewModel()
) {
    val chartData by viewModel.chartData.collectAsState()
    val selectedRange by viewModel.selectedTimeRange.collectAsState()

    val dateRangeText = remember(chartData, selectedRange) {
        if (chartData.isNotEmpty()) {
            val firstDate = chartData.first().dateFull
            val lastDate = chartData.last().dateFull

            when(selectedRange) {
                "Weekly" -> {
                    val f = SimpleDateFormat("MMM dd", Locale.getDefault())
                    "${f.format(firstDate)} - ${f.format(lastDate)}"
                }
                "Monthly" -> {
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(firstDate)
                }
                "Yearly" -> {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(firstDate)
                }
                else -> ""
            }
        } else {
            "Loading..."
        }
    }

    // MAIN LAYOUT
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // --- HEADER + FILTER + NAVIGATOR ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Report
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 74.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Report",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center
                )
            }

            // Time Filter (Weekly, Monthly, Yearly)
            TimeFilterSection(
                selectedRange = selectedRange,
                onRangeSelected = { viewModel.setTimeRange(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Date Navigator
            DateNavigator(
                dateText = dateRangeText,
                onPrevClick = { viewModel.previousPeriod() },
                onNextClick = { viewModel.nextPeriod() }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- CARD ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Chart Card 1
            ChartCardContainer(
                title = "Drink Completion",
                data = chartData,
                isPercentage = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Chart Card 2
            ChartCardContainer(
                title = "Hydrate",
                data = chartData,
                isPercentage = false
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
