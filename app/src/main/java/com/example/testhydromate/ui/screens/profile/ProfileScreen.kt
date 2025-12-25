package com.example.testhydromate.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.R
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit = {},
    onNavigateToMyProfile: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAboutApp: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 85.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(62.dp))

            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {

                // ===== MY PROFILE =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigateToMyProfile() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_profile),
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("My Profile", fontSize = 17.sp)
                }

                // ===== NOTIFICATIONS =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onNavigateToNotifications()
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_notif),
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(21.dp))
                    Text("Notifications", fontSize = 17.sp)
                }

                // ===== ABOUT APP =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigateToAboutApp() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_info),
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("About App", fontSize = 17.sp)
                }

                // ===== LOGOUT =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showLogoutDialog = true }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_logout),
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("Logout", fontSize = 17.sp)
                }
            }
        }

        // ===== LOGOUT DIALOG (Mature Style) =====
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = Color.White, // Background Putih Bersih
                tonalElevation = 0.dp, // Menghilangkan bayangan kebiruan default M3
                title = {
                    Text(
                        text = "Logout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E) // Warna teks hampir hitam agar elegan
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out of HydroMate? You'll need to sign in again to access your data.",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.logout()
                            onLogoutSuccess()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBA1A1A), // Merah gelap (Danger) yang lebih mature
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text("Logout", fontWeight = FontWeight.SemiBold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text(
                            "Cancel",
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp) // Sudut lebih melengkung agar modern
            )
        }

    }
}