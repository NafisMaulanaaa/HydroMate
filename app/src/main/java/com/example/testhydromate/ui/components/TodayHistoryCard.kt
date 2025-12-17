package com.example.testhydromate.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.utils.formatTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TodayHistoryCard(
    history: List<WaterLog>,
    modifier: Modifier = Modifier
) {
    val todayLogs = remember(history) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())

        history.filter {
            sdf.format(Date(it.timestamp)) == today
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {

        Column {

            // ===== TITLE =====
            Text(
                text = "Today History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // ===== EMPTY =====
            if (todayLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No drink history today",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            // ===== LIST (SAMA DENGAN HISTORY) =====
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    itemsIndexed(todayLogs) { index, log ->

                        TodayHistoryItem(log)

                        if (index < todayLogs.lastIndex) {
                            Divider(
                                modifier = Modifier.padding(start = 72.dp),
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayHistoryItem(log: WaterLog) {

    val timeFormat = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
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
            contentDescription = "More options",
            tint = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
