package com.example.testhydromate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// untuk di halaman onboarding
@Composable
fun TopBarOnBoardingPage(
    progress: Float,
    progressTxt: String,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showBackButton){
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFF2F4F8),
                modifier = Modifier.size(40.dp),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.padding(8.dp).size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f).height(8.dp),
            color = PrimaryBlue,
            trackColor = Color(0xFFE0E0E0),
            strokeCap = StrokeCap.Round,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(progressTxt, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun OptionButton(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            // Jika dipilih, border biru, jika tidak transparan
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) PrimaryBlue else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .background(LightGrayBg, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji Placeholder (Ganti dengan Image/Icon asli jika ada asset)
        Text(text = emoji, fontSize = 24.sp)

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
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