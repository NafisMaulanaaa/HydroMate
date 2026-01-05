package com.example.testhydromate.ui.screens.streak

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun StreakScreen(onBack: () -> Unit, streakCount: Int) {
    // Box utama untuk mengatur posisi elemen (Top & Bottom)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryBlue
                    )
                }
            }

            Text(
                text = "Streak",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Gambar Piala
            Image(
                painter = painterResource(id = com.example.testhydromate.R.drawable.trophy_1),
                modifier = Modifier.size(180.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Current Streak",
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$streakCount Day(s)",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        // Card diletakkan di bagian paling bawah
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF0F7FF)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Keep drinking water every day to grow your streak and stay healthy!",
                modifier = Modifier.padding(24.dp),
                textAlign = TextAlign.Center,
                color = PrimaryBlue,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }
    }
}
