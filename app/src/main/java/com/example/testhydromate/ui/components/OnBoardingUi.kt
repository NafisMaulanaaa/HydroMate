package com.example.testhydromate.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ========================
// ALL ON-BOARDING PAGE
// ========================
@Composable
fun OnboardingTopBar(
    currentStep: Int,
    onBackClick: (() -> Unit)? = null
) {
    val progress = when (currentStep) {
        1 -> 0.33f
        2 -> 0.66f
        3 -> 1f
        else -> 0f
    }

    val progressText = when (currentStep) {
        1 -> "1/3"
        2 -> "2/3"
        3 -> "3/3"
        else -> ""
    }

    val showBackButton = currentStep > 1

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryBlue
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PrimaryBlue,
            trackColor = Color(0xFFE0E0E0)
        )

        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = progressText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ========================
// INPUT PERSONAL
// ========================

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

// ========================
// INPUT HABIT AND WEATHER
// ========================
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