package com.example.testhydromate.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.data.model.DailyChartData
import com.example.testhydromate.ui.components.ChartCardContainer
import com.example.testhydromate.ui.components.ChartType
import com.example.testhydromate.ui.components.DateNavigator
import com.example.testhydromate.ui.components.HydrationChart
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

    // Logic teks tanggal agar sesuai filter
    val dateRangeText = remember(chartData, selectedRange) {
        if (chartData.isNotEmpty()) {
            val firstDate = chartData.first().dateFull
            val lastDate = chartData.last().dateFull

            when(selectedRange) {
                "Weekly" -> {
                    // Dec 16 - Dec 22
                    val f = SimpleDateFormat("MMM dd", Locale.getDefault())
                    "${f.format(firstDate)} - ${f.format(lastDate)}"
                }
                "Monthly" -> {
                    // December 2025
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(firstDate)
                }
                "Yearly" -> {
                    // 2025
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(firstDate)
                }
                else -> ""
            }
        } else {
            "Loading..."
        }
    }

    // MAIN LAYOUT (Tanpa Scroll di root)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // --- 1. HEADER (STATIK / DIAM) ---
        // Posisinya di luar scrollable content
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

        // --- 2. KONTEN (SCROLLABLE) ---
        // Gunakan weight(1f) agar mengambil sisa ruang di bawah header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Penting! Agar konten mengisi sisa layar
                .verticalScroll(rememberScrollState()), // Scroll ditaruh di sini
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- TIME FILTER ---
            TimeFilterSection(
                selectedRange = selectedRange,
                onRangeSelected = { viewModel.setTimeRange(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- DATE NAVIGATOR ---
            DateNavigator(
                dateText = dateRangeText,
                onPrevClick = { viewModel.previousPeriod() },
                onNextClick = { viewModel.nextPeriod() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- CHARTS ---
            ChartCardContainer(
                title = "Drink Completion",
                data = chartData,
                isPercentage = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            ChartCardContainer(
                title = "Hydrate",
                data = chartData,
                isPercentage = false
            )

            // Padding bawah ekstra agar tidak tertutup BottomBar
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


