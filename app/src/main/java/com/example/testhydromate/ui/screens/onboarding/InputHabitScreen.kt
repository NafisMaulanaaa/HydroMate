package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.LightGrayBg
import com.example.testhydromate.ui.components.TopBarOnBoardingPage
import com.example.testhydromate.ui.components.OptionButton
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray

@Composable
fun InputHabitScreen(onContinueClicked: () -> Unit, onBackClicked:() -> Unit) {
    // --- State Variables ---
    // Waktu Bangun
    var wakeHour by remember { mutableStateOf("") }
    var wakeMinute by remember { mutableStateOf("") }

    // Waktu Tidur
    var bedHour by remember { mutableStateOf("") } // 22 = 10 PM
    var bedMinute by remember { mutableStateOf("") }

    // Pilihan Aktivitas (State untuk menampung mana yang dipilih)
    var selectedActivity by remember { mutableStateOf("") }

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

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Waktu Bangun (Wake Up)
            TimeInputSection(
                label = "What time you usually wake up?",
                hourValue = wakeHour,
                minuteValue = wakeMinute,
                amPmLabel = "AM", // Logika AM/PM bisa dibuat dinamis jika perlu
                onHourChange = { wakeHour = it },
                onMinuteChange = { wakeMinute = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Waktu Tidur (Bed Time)
            TimeInputSection(
                label = "What time you usually go to bed?",
                hourValue = bedHour,
                minuteValue = bedMinute,
                amPmLabel = "PM",
                onHourChange = { bedHour = it },
                onMinuteChange = { bedMinute = it }
            )

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
                emoji = "🚶", // Menggunakan Emoji sebagai placeholder icon
                isSelected = selectedActivity == "Light Activity",
                onClick = { selectedActivity = "Light Activity" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OptionButton(
                text = "Moderate Active",
                emoji = "🏃",
                isSelected = selectedActivity == "Moderate Active",
                onClick = { selectedActivity = "Moderate Active" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OptionButton(
                text = "Very Active",
                emoji = "🏋️",
                isSelected = selectedActivity == "Very Active",
                onClick = { selectedActivity = "Very Active" }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun TimeInputSection(
    label: String,
    hourValue: String,
    minuteValue: String,
    amPmLabel: String,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF424242),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Kotak Jam
            TimeBox(value = hourValue, onValueChange = onHourChange)

            Text(
                text = ":",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // Kotak Menit
            TimeBox(value = minuteValue, onValueChange = onMinuteChange)

            Spacer(modifier = Modifier.width(16.dp))

            // Label AM/PM
            Text(
                text = amPmLabel,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )
        }
    }
}

@Composable
fun TimeBox(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(56.dp)
            .background(LightGrayBg, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HabitsPreview() {
    InputHabitScreen(onContinueClicked = {}, onBackClicked = {})
}