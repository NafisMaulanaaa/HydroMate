package com.example.testhydromate.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.components.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val logs by viewModel.waterLogs.collectAsState()

    val groupedLogs = logs.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(it.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 12.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 100.dp // ruang aman navbar (dari route)
            )
    ) {

        // --- 1. Spacer Atas ---
        Spacer(modifier = Modifier.height(32.dp))

        // ===== TITLE =====
        Text(
            text = "History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ===== HISTORY LIST =====
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            groupedLogs.forEach { (date, items) ->

                item {
                    Text(
                        text = formatDateHeader(date),
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF7F7F7)
                        )
                    ) {
                        Column {
                            items.forEachIndexed { index, log ->
                                HistoryItem(log)
                                if (index != items.lastIndex) {
                                    Divider(
                                        modifier = Modifier.padding(start = 72.dp),
                                        color = Color(0xFFE0E0E0)
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun HistoryItem(log: WaterLog) {

    val timeFormat = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Water",
                fontWeight = FontWeight.Medium
            )
            Text(
                text = timeFormat.format(Date(log.timestamp)),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "${log.amount} mL",
            fontWeight = FontWeight.Medium
        )

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


private fun formatDateHeader(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateObj = sdf.parse(date) ?: return date

    val today = sdf.format(Date())
    val cal = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    val yesterday = sdf.format(cal.time)

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(dateObj)
    }
}
