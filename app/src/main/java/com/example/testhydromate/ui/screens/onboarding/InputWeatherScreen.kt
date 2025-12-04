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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.OptionButton
import com.example.testhydromate.ui.components.TopBarOnBoardingPage
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray

@Composable
fun InputWeatherScreen(onContinueClicked: () -> Unit, onBackClicked:() -> Unit) {
    // State untuk menyimpan pilihan cuaca
    var selectedWeather by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // Tombol Continue
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

            // Top Bar (Page 3/3)
            TopBarOnBoardingPage(progress = 1f, progressTxt = "3/3", showBackButton = true, onBackClick = onBackClicked )

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

            // Pilih Weather
            OptionButton(
                text = "Hot",
                emoji = "☀️",
                isSelected = selectedWeather == "Hot",
                onClick = { selectedWeather = "Hot" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OptionButton(
                text = "Temperate",
                emoji = "⛅",
                isSelected = selectedWeather == "Temperate",
                onClick = { selectedWeather = "Temperate" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OptionButton(
                text = "Cold",
                emoji = "🌨️",
                isSelected = selectedWeather == "Cold",
                onClick = { selectedWeather = "Cold" }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun WeatherInputPreview() {
    InputWeatherScreen(onContinueClicked = {}, onBackClicked = {})
}