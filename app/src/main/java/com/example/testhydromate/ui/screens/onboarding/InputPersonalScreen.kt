package com.example.testhydromate.ui.screens.onboarding

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.FemalePink
import com.example.testhydromate.ui.components.GenderButton
import com.example.testhydromate.ui.components.MeasurementInput
import com.example.testhydromate.ui.components.OnboardingTopBar
import com.example.testhydromate.ui.components.TextGray

@Composable
fun InputPersonalScreen(
    viewModel: OnboardingViewModel,
    onContinueClicked: () -> Unit,
    onBackClicked: (() -> Unit)? = null
) {
    // 1. Ambil Context untuk menampilkan Toast
    val context = LocalContext.current

    // Kita gunakan state langsung dari ViewModel
    // viewModel.gender, viewModel.height, dll.

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            HydroPrimaryButton(
                text = "Continue",
                onClick = {
                    // 2. LOGIKA VALIDASI
                    if (viewModel.gender.isNotBlank() &&
                        viewModel.height.isNotBlank() &&
                        viewModel.weight.isNotBlank() &&
                        viewModel.age.isNotBlank()
                    ) {
                        // Jika semua terisi, lanjut navigasi
                        onContinueClicked()
                    } else {
                        // Jika ada yang kosong, munculkan pesan
                        Toast.makeText(context, "Please complete all fields to continue", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Bar
            OnboardingTopBar(
                currentStep = 1,
                onBackClick = null
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Header Title
            Text(
                text = "Personal Details",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Complete your personal info to set your water intake.",
                fontSize = 14.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Gender Section
            Text(
                text = "Gender",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Perhatikan: viewModel.gender diupdate di onClick
                GenderButton(
                    text = "Male",
                    icon = Icons.Default.Male,
                    isSelected = viewModel.gender == "Male",
                    color = PrimaryBlue,
                    scndColor = Color(0xFFD3EAFF),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.gender = "Male" }
                )
                GenderButton(
                    text = "Female",
                    icon = Icons.Default.Female,
                    isSelected = viewModel.gender == "Female",
                    color = FemalePink,
                    scndColor = Color(0xFFFFF1FA),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.gender = "Female" }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields (Height, Weight, Age)
            MeasurementInput(
                label = "Height",
                value = viewModel.height,
                unit = "cm",
                onValueChange = { if (it.length <= 3) viewModel.height = it } // Batasi panjang input biar rapi
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasurementInput(
                label = "Weight",
                value = viewModel.weight,
                unit = "kg",
                onValueChange = { if (it.length <= 3) viewModel.weight = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasurementInput(
                label = "Age",
                value = viewModel.age,
                unit = "y.o.",
                onValueChange = { if (it.length <= 3) viewModel.age = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}