package com.example.testhydromate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Get Started now",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color(0xff0e61d1),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Welcome to HydroMate! Create an account or log in to track your water intake, set reminders, and get hydrate!",
            fontSize = 12.sp,
            color = Color(0xff6c7278),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AuthTabSelector(
    isLogin: Boolean,
    onTabSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xfff5f6f9), RoundedCornerShape(8.dp))
            .padding(3.dp)
    ) {
        // Tab Helper
        @Composable
        fun AuthTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isSelected) Color.White else Color.Transparent,
                        RoundedCornerShape(6.dp)
                    )
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.Black else Color(0xff7d7d91)
                )
            }
        }

        AuthTab(text = "Log In", isSelected = isLogin, onClick = { onTabSelected(true) })
        AuthTab(text = "Sign Up", isSelected = !isLogin, onClick = { onTabSelected(false) })
    }
}