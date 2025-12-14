package com.example.testhydromate.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.HydroBottomBar
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 85.dp // Ruang untuk navbar
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. Spacer Atas ---
            Spacer(modifier = Modifier.height(72.dp))

            // ===== TITLE =====
            Text(
                text = "Profile", // Ganti Judul jadi Profile
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            // --- Spacer Pendorong agar tombol ke tengah ---
            Spacer(modifier = Modifier.weight(1f))

            // ===== LOGOUT BUTTON =====
            Button(
                onClick = {
                    viewModel.logout()
                    onLogoutSuccess()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error // Warna merah untuk logout
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Lebar tombol 50% layar
                    .height(50.dp)
            ) {
                Text(
                    text = "Logout",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // --- Spacer Pendorong Bawah ---
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
