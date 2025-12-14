package com.example.testhydromate.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R

@Composable
fun WaterProgress(
    current: Int,
    target: Int
) {
    val progress = (current.toFloat() / target).coerceIn(0f, 1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {

        Canvas(
            modifier = Modifier
                .size(240.dp)
        ) {
            val strokeWidth = 26.dp.toPx()
            val arcSize = Size(size.width, size.height)

            // Kita ingin total lengkungan 245 derajat.
            // Agar simetris (tengahnya di atas/jam 12), kita mulai dari 147.5 derajat.
            // Rumus: 270 (atas) - (TotalSudut / 2) -> 270 - 122.5 = 147.5
            val startAngleVal = 147.5f
            val sweepAngleVal = 245f

            // background arc (abu-abu)
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = startAngleVal,
                sweepAngle = sweepAngleVal,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            // progress arc (biru)
            drawArc(
                color = PrimaryBlue,
                startAngle = startAngleVal,
                sweepAngle = sweepAngleVal * progress, // Progress dikalikan 245 derajat
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.height(64.dp))

            Icon(
                painter = painterResource(id = R.drawable.water),
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                tint = Color.Unspecified // <--- Gunakan ini agar warna asli gambar muncul
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "$current",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            Text(
                text = "/$target mL",
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}
