package com.example.testhydromate.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrinkButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(48.dp)
            .width(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp, // Ukuran font diperbesar sesuai permintaan
            fontWeight = FontWeight.Bold, // Ditambah bold biar lebih mantap
            color = Color.White
        )
    }
}
