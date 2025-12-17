package com.example.testhydromate.ui.screens.profile

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.R
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun MyProfile(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("Cecep Knalpot") }
    var selectedGender by remember { mutableStateOf("Male") }
    var email by remember { mutableStateOf("Cecepknalpot@gmail.com") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

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

            Spacer(modifier = Modifier.height(32.dp))

            // Top Bar with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryBlue
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // Spacer Atas
            //Spacer(modifier = Modifier.height(16.dp))

            // Title My Profile
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Basic Detail Section
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // Full Name
                        InputField(
                            label = "Full name",
                            value = fullName,
                            onValueChange = { fullName = it }
                        )

                        // Gender Selection
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Gender",
                                color = Color(0xFF333333),
                                fontSize = 16.sp
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Male Button
                                GenderButton(
                                    text = "Male",
                                    isSelected = selectedGender == "Male",
                                    color = Color(0xFF0E61D1),
                                    onClick = { selectedGender = "Male" },
                                    modifier = Modifier.weight(1f)
                                )

                                // Female Button
                                GenderButton(
                                    text = "Female",
                                    isSelected = selectedGender == "Female",
                                    color = Color(0xFFD10E79),
                                    onClick = { selectedGender = "Female" },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Contact Detail Section
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Contact Detail",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Email
                        InputField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it }
                        )
                    }
                }

                // Save Button
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(PrimaryBlue)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                Toast.makeText(
                                    context,
                                    "Your Information has been saved.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color(0xFF999999),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C1E)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
    }
}

@Composable
fun GenderButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) color.copy(alpha = 0.1f) else Color.White)
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if (isSelected) color else Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                color = if (isSelected) color else Color(0xFF999999),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}