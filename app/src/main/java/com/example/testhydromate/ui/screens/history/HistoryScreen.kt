package com.example.testhydromate.ui.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.HydroBottomBar
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun HistoryScreen(
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ===== CONTENT (Teks di Tengah) =====
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "This is History page",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}
