package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.TopBarOnBoardingPage
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.FemalePink
import com.example.testhydromate.ui.components.LightGrayBg
import com.example.testhydromate.ui.components.OnboardingTopBar
import com.example.testhydromate.ui.components.TextGray


@Composable
fun InputPersonalScreen(
    viewModel: OnboardingViewModel,
    onContinueClicked: () -> Unit,
    onBackClicked: (() -> Unit)? = null
) {
    var selectedGender by remember { mutableStateOf("") }
//    var height by remember { mutableStateOf("") }
//    var weight by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            HydroPrimaryButton(text = "Continue", onClick = onContinueClicked)
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
                GenderButton(
                    text = "Male",
                    icon = Icons.Default.Male,
                    isSelected = selectedGender == "Male",
                    color = PrimaryBlue,
                    scndColor = Color(0xFFD3EAFF),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedGender = "Male"
                        viewModel.gender = "Male"
                    }
                )
                GenderButton(
                    text = "Female",
                    icon = Icons.Default.Female,
                    isSelected = selectedGender == "Female",
                    color = FemalePink,
                    scndColor = Color(0xFFFFF1FA),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedGender = "Female"
                        viewModel.gender = "Female"
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields (Height, Weight, Age)
            MeasurementInput(
                label = "Height",
                value = viewModel.height,
                unit = "cm",
                onValueChange = { viewModel.height = it}
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasurementInput(
                label = "Weight",
                value = viewModel.weight,
                unit = "kg",
                onValueChange = { viewModel.weight = it}
            )

            Spacer(modifier = Modifier.height(16.dp))

            MeasurementInput(
                label = "Age",
                value = viewModel.age,
                unit = "y.o.",
                onValueChange = { viewModel.age = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GenderButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    color: Color,
    scndColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSelected) scndColor else Color.Transparent)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (isSelected) color else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = if (isSelected) color else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun MeasurementInput(
    label: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Custom Styled Input Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(LightGrayBg, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Kita pakai Box untuk menengahkan Text Field secara presisi
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            Text(
                text = unit,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

// --- Preview ---
//@Preview(showBackground = true, widthDp = 390, heightDp = 844)
//@Composable
//fun PersonalDetailScreenPreview() {
//    InputPersonalScreen(viewModel = {  }, onContinueClicked = {})
//}