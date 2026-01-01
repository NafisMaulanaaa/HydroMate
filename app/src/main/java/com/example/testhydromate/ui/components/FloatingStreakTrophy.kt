package com.example.testhydromate.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FloatingStreakTrophy(
    streakCount: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Ukuran layar dalam pixels
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Ukuran trophy
    val trophySize = with(density) { 60.dp.toPx() }
    val margin = with(density) { 16.dp.toPx() }
    val topMargin = with(density) {75.dp.toPx() } // Jarak dari atas biar ga nabrak header

    // ✅ DEFAULT POSISI: KANAN ATAS
    var offsetX by remember { mutableStateOf(screenWidthPx - trophySize - margin) }
    var offsetY by remember { mutableStateOf(topMargin) }

    // Untuk animasi smooth snap ke pojok
    val animatedOffsetX = remember { Animatable(offsetX) }
    val animatedOffsetY = remember { Animatable(offsetY) }
    val coroutineScope = rememberCoroutineScope()

    // Animasi Glowing
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // Fungsi untuk snap ke pojok terdekat
    fun snapToNearestCorner() {
        val centerX = animatedOffsetX.value + trophySize / 2
        val centerY = animatedOffsetY.value + trophySize / 2

        // Tentukan pojok terdekat
        val targetX = if (centerX < screenWidthPx / 2) {
            margin // Kiri
        } else {
            screenWidthPx - trophySize - margin // Kanan
        }

        val targetY = if (centerY < screenHeightPx / 2) {
            topMargin // Atas (dengan top margin)
        } else {
            screenHeightPx - trophySize - margin // Bawah
        }

        // Animasi smooth ke pojok
        coroutineScope.launch {
            launch {
                animatedOffsetX.animateTo(
                    targetValue = targetX,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                animatedOffsetY.animateTo(
                    targetValue = targetY,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        offsetX = targetX
        offsetY = targetY
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    animatedOffsetX.value.roundToInt(),
                    animatedOffsetY.value.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Snap ke pojok saat drag selesai
                        snapToNearestCorner()
                    }
                ) { change, dragAmount ->
                    change.consume()
                    val newX = (animatedOffsetX.value + dragAmount.x)
                        .coerceIn(0f, screenWidthPx - trophySize)
                    val newY = (animatedOffsetY.value + dragAmount.y)
                        .coerceIn(topMargin, screenHeightPx - trophySize)

                    coroutineScope.launch {
                        animatedOffsetX.snapTo(newX)
                        animatedOffsetY.snapTo(newY)
                    }
                }
            }
            .size(50.dp),
        contentAlignment = Alignment.Center
    ) {
        // Efek Sinar (Hanya muncul jika isActive true)
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.Yellow.copy(alpha = glowAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Gambar Trophy
        Image(
            painter = painterResource(id = R.drawable.trophy_1),
            contentDescription = "Streak Trophy",
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                }
        )

        // Angka Streak kecil di pojok piala
//        Badge(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            containerColor = Color(0xFF2196F3) // Ganti dengan PrimaryBlue Anda
//        ) {
//            Text("$streakCount", color = Color.White, fontSize = 10.sp)
//        }
    }
}