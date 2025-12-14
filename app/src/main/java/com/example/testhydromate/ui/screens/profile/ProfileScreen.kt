package com.example.testhydromate.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.R
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
                    bottom = 85.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer Atas
            Spacer(modifier = Modifier.height(72.dp))

            // Title Settings
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Profile Section
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Profile Image Placeholder
                Box(
                    modifier = Modifier
                        .size(73.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD9D9D9))
                )

                // Profile Info
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Cecep Knalpot",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cecepknalpot@gmail.com",
                        color = Color(0xFF333333),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Items
            Column(
                verticalArrangement = Arrangement.spacedBy(48.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                // My Profile
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_profile),
                        contentDescription = "My Profile",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "My Profile",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                // Notifications
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_notif),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Notifications",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                // About App
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_info),
                        contentDescription = "About App",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "About App",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                // Logout
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}