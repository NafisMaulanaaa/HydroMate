package com.example.testhydromate.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.GenderButton
import com.example.testhydromate.ui.components.InputField
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.ProfileGenderButton

@Composable
fun MyProfile(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val context = LocalContext.current
    val user = viewModel.userData
    val isLoading = viewModel.isLoading

    LaunchedEffect(user) {
        user?.let {
            fullName = "${it.firstName} ${it.lastName}".trim()
            selectedGender = it.gender
            email = it.email
            age = it.age.toString()
            weight = it.weight.toString()
            height = it.height.toString()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PrimaryBlue
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar & Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    // Nama & Gender
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InputField(label = "Full name", value = fullName, onValueChange = { fullName = it })

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

                    // Age, Weight, Height (Dibuat berdampingan agar rapi)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(label = "Age", value = age, onValueChange = { age = it })
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(label = "Weight (kg)", value = weight, onValueChange = { weight = it })
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                InputField(label = "Height (cm)", value = height, onValueChange = { height = it })
                            }
                        }
                    }

                    //  Email
                    item {
                        InputField(label = "Email", value = email, onValueChange = { email = it })
                    }

                    // Save Button
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                Toast.makeText(context, "Information saved locally", Toast.LENGTH_SHORT).show()
                                // Di sini nantinya kamu panggil viewModel.updateUser(...)
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("Save", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
