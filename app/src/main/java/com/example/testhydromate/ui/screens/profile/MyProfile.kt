package com.example.testhydromate.ui.screens.profile

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.InputField
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.ProfileGenderButton
import kotlinx.coroutines.delay

@Composable
fun MyProfile(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // States untuk form input
    var fullName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    // States untuk Notifikasi
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val user = viewModel.userData
    val isLoading = viewModel.isLoading

    // Load data awal dari database
    LaunchedEffect(user) {
        user?.let {
            fullName = "${it.firstName} ${it.lastName}".trim()
            selectedGender = it.gender
            email = it.email
            age = if (it.age > 0) it.age.toString() else ""
            weight = if (it.weight > 0) it.weight.toString() else ""
            height = if (it.height > 0) it.height.toString() else ""
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryBlue
                    )
                }
            }

            Text(
                text = "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (isLoading && user == null) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InputField(
                                label = "Full name",
                                value = fullName,
                                onValueChange = { fullName = it }
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Gender", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    ProfileGenderButton(
                                        text = "Male", icon = Icons.Default.Male,
                                        isSelected = selectedGender.equals("Male", true),
                                        color = Color(0xFF0E61D1), onClick = { selectedGender = "Male" },
                                        modifier = Modifier.weight(1f)
                                    )
                                    ProfileGenderButton(
                                        text = "Female", icon = Icons.Default.Female,
                                        isSelected = selectedGender.equals("Female", true),
                                        color = Color(0xFFD10E79), onClick = { selectedGender = "Female" },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(
                                    label = "Age",
                                    value = age,
                                    onValueChange = { if (it.all { char -> char.isDigit() }) age = it }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(
                                    label = "Weight (kg)",
                                    value = weight,
                                    onValueChange = { weight = it }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(
                                    label = "Height (cm)",
                                    value = height,
                                    onValueChange = { height = it }
                                )
                            }
                        }
                    }

                    item {
                        InputField(
                            label = "Email",
                            value = email,
                            onValueChange = { }
                            // Parameter readOnly dihapus karena tidak ada di komponen InputField kamu
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (fullName.isBlank()) {
                                    notificationMessage = "Name cannot be empty"
                                    isError = true
                                    showNotification = true
                                    return@Button
                                }

                                viewModel.updateUserProfile(
                                    fullName = fullName,
                                    gender = selectedGender,
                                    age = age,
                                    weight = weight,
                                    height = height,
                                    onSuccess = {
                                        notificationMessage = "Profile updated successfully!"
                                        isError = false
                                        showNotification = true
                                    },
                                    onError = { error ->
                                        notificationMessage = error
                                        isError = true
                                        showNotification = true
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Save Changes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- CUSTOM NOTIFICATION ---
        AnimatedVisibility(
            visible = showNotification,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = if (isError) Color(0xFFE53935) else Color(0xFF4CAF50),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = notificationMessage,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            LaunchedEffect(showNotification) {
                if (showNotification) {
                    delay(3000)
                    showNotification = false
                }
            }
        }
    }
}
