package com.example.testhydromate.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.testhydromate.data.model.DrinkHistory
import com.example.testhydromate.ui.utils.formatTime

@Composable
fun TodayHistoryCard(
    history: List<DrinkHistory>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Text(
            text = "No drink history today",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- HEADER (DIAM / TIDAK SCROLL) ---
            Text(
                text = "Today History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Menggunakan LazyColumn di sini agar hanya list ini yang scroll
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(history) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Water",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = formatTime(item.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "${item.amount} mL",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Divider (kecuali item terakhir)
                    if (index < history.size - 1) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
