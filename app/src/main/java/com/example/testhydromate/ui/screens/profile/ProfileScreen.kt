package com.example.testhydromate.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit = {},
    onNavigateToMyProfile: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToReminderSetting: () -> Unit = {},
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

                // ===== MY PROFILE (Icon: Person) =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigateToMyProfile() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        tint = PrimaryBlue, // Bisa diganti Color.Gray atau Color.Black
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("My Profile", fontSize = 17.sp)
                }

                // ===== NOTIFICATIONS (Icon: Notifications) =====
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
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("Notifications", fontSize = 17.sp)
                }

                // ===== REMINDER SETTINGS (Icon: Alarm) =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onNavigateToReminderSetting()
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Alarm, // Menggunakan icon Alarm/Jam
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("Reminder", fontSize = 17.sp)
                }

                // ===== ABOUT APP (Icon: Info) =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigateToAboutApp() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("About App", fontSize = 17.sp)
                }

                // ===== LOGOUT (Icon: Logout) =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showLogoutDialog = true }
                ) {
                    Icon(
                        // AutoMirrored: Icon otomatis membalik arah jika HP user pakai bahasa Arab/RTL
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        tint = Color.Red, // Logout biasanya merah, atau ganti PrimaryBlue
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text("Logout", fontSize = 17.sp, color = Color.Red) // Teks juga merah biar serasi
                }
            }
        }

        // ===== LOGOUT DIALOG =====
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = Color.White,
                tonalElevation = 0.dp,
                title = {
                    Text(
                        text = "Logout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
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
                            containerColor = Color(0xFFBA1A1A),
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
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}