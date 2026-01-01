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
import com.example.testhydromate.ui.components.ChartType
import com.example.testhydromate.ui.components.HydrationChart
import com.example.testhydromate.ui.components.PrimaryBlue
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

// --- COMPONENTS ---
// (Bagian komponen di bawah ini SAMA PERSIS dengan kode Anda sebelumnya)

@Composable
fun TimeFilterSection(
    selectedRange: String,
    onRangeSelected: (String) -> Unit
) {
    val options = listOf("Weekly", "Monthly", "Yearly")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(100)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selectedRange
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(100))
                    .background(if (isSelected) PrimaryBlue else Color.Transparent)
                    .clickable { onRangeSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun DateNavigator(
    dateText: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevClick) {
            Icon(Icons.Default.ChevronLeft, null, tint = Color.Gray)
        }

        Text(
            text = dateText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

@Composable
fun ChartCardContainer(
    title: String,
    data: List<DailyChartData>,
    isPercentage: Boolean
) {
    var currentChartType by remember { mutableStateOf(ChartType.BAR) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    ChartToggleButton(
                        icon = Icons.Default.BarChart,
                        isSelected = currentChartType == ChartType.BAR,
                        onClick = { currentChartType = ChartType.BAR }
                    )
                    ChartToggleButton(
                        icon = Icons.Default.ShowChart,
                        isSelected = currentChartType == ChartType.LINE,
                        onClick = { currentChartType = ChartType.LINE }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (data.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Data Available", color = Color.Gray)
                }
            } else {
                HydrationChart(
                    data = data,
                    type = currentChartType,
                    isPercentage = isPercentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun ChartToggleButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) PrimaryBlue else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}