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
import com.example.testhydromate.ui.components.TopBarOnBoardingPage
import com.example.testhydromate.ui.components.OptionButton
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray

@Composable
fun InputHabitScreen(
    viewModel: OnboardingViewModel,
    onContinueClicked: () -> Unit,
    onBackClicked:() -> Unit,
    ) {
//    var wakeHour by remember { mutableStateOf("") }
//    var wakeMinute by remember { mutableStateOf("") }
//
//    var bedHour by remember { mutableStateOf("") }
//    var bedMinute by remember { mutableStateOf("") }
//
//    var selectedActivity by remember { mutableStateOf("") }

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
            TopBarOnBoardingPage(progress = 0.66f, progressTxt =  "2/3", showBackButton = true, onBackClick = onBackClicked)

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Header
            Text(
                text = "Input Your Habits",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Input your daily habit to set your water intake based on your habit.",
                fontSize = 14.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // 3. Waktu Bangun (Wake Up)
//            TimeInputSection(
//                label = "What time you usually wake up?",
//                hourValue = viewModel.wakeHour,
//                minuteValue = viewModel.wakeMinute,
//                amPmLabel = "AM", // Logika AM/PM bisa dibuat dinamis jika perlu
//                onHourChange = { viewModel.wakeHour = it },
//                onMinuteChange = { viewModel.wakeMinute = it }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // 4. Waktu Tidur (Bed Time)
//            TimeInputSection(
//                label = "What time you usually go to bed?",
//                hourValue = viewModel.bedHour,
//                minuteValue = viewModel.bedMinute,
//                amPmLabel = "PM",
//                onHourChange = { viewModel.bedHour = it },
//                onMinuteChange = { viewModel.bedMinute = it }
//            )

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Activity Level
            Text(
                text = "What your activity level?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // List Pilihan Aktivitas
            OptionButton(
                text = "Light Activity",
                emoji = "🚶",
                isSelected = viewModel.activityLevel == "Light Activity",
                onClick = { viewModel.activityLevel = "Light Activity" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OptionButton(
                text = "Moderate Active",
                emoji = "🏃",
                isSelected = viewModel.activityLevel == "Moderate Active",
                onClick = { viewModel.activityLevel = "Moderate Active" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OptionButton(
                text = "Very Active",
                emoji = "🏋️",
                isSelected = viewModel.activityLevel == "Very Active",
                onClick = { viewModel.activityLevel = "Very Active" }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}




//@Preview(showBackground = true, widthDp = 390, heightDp = 844)
//@Composable
//fun HabitsPreview() {
//    InputHabitScreen(onContinueClicked = {}, onBackClicked = {})
//}