package com.example.testhydromate.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    target: Int,
    onTargetClick: () -> Unit // <--- Callback baru
) {

    // 🔥 ANIMATED PROGRESS
    val animatedProgress by animateFloatAsState(
        targetValue = (current.toFloat() / target).coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "water_progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {

        Canvas(modifier = Modifier.size(240.dp)) {
            val strokeWidth = 26.dp.toPx()
            val arcSize = Size(size.width, size.height)
            val startAngleVal = 147.5f
            val sweepAngleVal = 245f

            // Background arc
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = startAngleVal,
                sweepAngle = sweepAngleVal,
                useCenter = false,
                topLeft = Offset.Zero,
                size = arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = PrimaryBlue,
                startAngle = startAngleVal,
                sweepAngle = sweepAngleVal * animatedProgress,
                useCenter = false,
                topLeft = Offset.Zero,
                size = arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            Icon(
                painter = painterResource(id = R.drawable.water),
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                tint = Color.Unspecified
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "$current",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            // BARIS TARGET YANG BISA DIKLIK
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onTargetClick() } // Klik trigger bottom sheet
                    .padding(4.dp)
            ) {
                Text(
                    text = "/$target mL",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Goal",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}