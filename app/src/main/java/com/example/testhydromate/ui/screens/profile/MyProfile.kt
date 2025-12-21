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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun MyProfile(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // State lokal untuk input field
    var fullName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val user = viewModel.userData
    val isLoading = viewModel.isLoading

    // Sync data dari Firebase ke State Lokal saat data berhasil dimuat
    LaunchedEffect(user) {
        user?.let {
            fullName = "${it.firstName} ${it.lastName}".trim()
            selectedGender = it.gender
            email = it.email
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            // Loading Indicator
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PrimaryBlue
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 85.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
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

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InputField(
                                label = "Full name",
                                value = fullName,
                                onValueChange = { fullName = it }
                            )

                            // Gender Selection
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Gender", color = Color(0xFF333333), fontSize = 16.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    GenderButton(
                                        text = "Male",
                                        isSelected = selectedGender.equals("Male", true),
                                        color = Color(0xFF0E61D1),
                                        onClick = { selectedGender = "Male" },
                                        modifier = Modifier.weight(1f)
                                    )
                                    GenderButton(
                                        text = "Female",
                                        isSelected = selectedGender.equals("Female", true),
                                        color = Color(0xFFD10E79),
                                        onClick = { selectedGender = "Female" },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Contact Detail", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            InputField(
                                label = "Email",
                                value = email,
                                onValueChange = { email = it }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                Toast.makeText(context, "Information saved locally", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
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

// Komponen InputField dan GenderButton tetap sama seperti kode awal Anda
@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color(0xFF999999), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF1A1C1E)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
    }
}

@Composable
fun GenderButton(text: String, isSelected: Boolean, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) color.copy(alpha = 0.1f) else Color.White)
            .border(BorderStroke(2.dp, if (isSelected) color else Color(0xFFE0E0E0)), RoundedCornerShape(10.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
    ) {
        Text(text = text, color = if (isSelected) color else Color(0xFF999999), fontWeight = FontWeight.Medium)
    }
}