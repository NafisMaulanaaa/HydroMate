package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.OnboardingTopBar
import com.example.testhydromate.ui.components.OptionButton
//import com.example.testhydromate.ui.components.TopBarOnBoardingPage
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray

@Composable
fun InputWeatherScreen(
    viewModel: OnboardingViewModel,
    onContinueClicked: () -> Unit,
    onBackClicked:() -> Unit
) {

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            HydroPrimaryButton(
                text = "Generate plan!",
                onClick = {
//                    viewModel.saveOnboardingData()
                    onContinueClicked()
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
                currentStep = 3,
                onBackClick = onBackClicked
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Header
            Text(
                text = "What's weather like?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "External factors like weather can influence your hydration needs.",
                fontSize = 14.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OptionButton(
                text = "Hot",
                emoji = "☀️",
                isSelected = viewModel.weather == "Hot",
                onClick = { viewModel.weather = "Hot" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OptionButton(
                text = "Temperate",
                emoji = "⛅",
                isSelected = viewModel.weather == "Temperate",
                onClick = { viewModel.weather = "Temperate" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OptionButton(
                text = "Cold",
                emoji = "🌨️",
                isSelected = viewModel.weather == "Cold",
                onClick = { viewModel.weather = "Cold" }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

//@Preview(showBackground = true, widthDp = 390, heightDp = 844)
//@Composable
//fun WeatherInputPreview() {
//    InputWeatherScreen(onContinueClicked = {}, onBackClicked = {})
//}