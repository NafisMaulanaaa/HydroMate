package com.example.testhydromate.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HydroBottomBar(
    selectedIndex: Int,
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(320.dp)
            .padding(horizontal = 16.dp)
            .padding(bottom = 25.dp)
            .height(64.dp),
        shape = RoundedCornerShape(120.dp),
        color = Color(0xffFBFBFB),
        shadowElevation = 12.dp
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp), // Padding dalam surface
            contentAlignment = Alignment.CenterStart
        ) {
            val maxWidth = maxWidth
            val itemCount = 3
            // Lebar satu segmen/item
            val itemWidth = maxWidth / itemCount

            // 1. ANIMASI GESER (OFFSET)
            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * selectedIndex,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "indicator_slide"
            )

            // 2. BACKGROUND BIRU (SLIDING INDICATOR)
            // Ini dirender DULUAN agar berada di BELAKANG icon
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset) // Geser posisi X
                    .width(itemWidth) // Lebar area indikator sama dengan lebar 1 item
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center // Pastikan pill biru di tengah area
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(72.dp) // Ukuran fix pill biru
                        .background(
                            color = PrimaryBlue,
                            shape = RoundedCornerShape(24.dp)
                        )
                )
            }

            // 3. ROW ICON (DI ATAS BACKGROUND)
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomItem(
                    icon = Icons.Default.Home,
                    selected = selectedIndex == 0,
                    onClick = onHome,
                    modifier = Modifier.weight(1f)
                )

                BottomItem(
                    icon = Icons.Default.Description,
                    selected = selectedIndex == 1,
                    onClick = onHistory,
                    modifier = Modifier.weight(1f)
                )

                BottomItem(
                    icon = Icons.Default.Person,
                    selected = selectedIndex == 2,
                    onClick = onProfile,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Interaction Source & State Tekan (untuk efek scale)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animasi Scale (Mengecil saat ditekan)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        label = "scale_anim"
    )

    // Animasi Warna Icon (Biar transisinya smooth ngikutin background)
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xff9E9E9E),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "color_anim"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Hapus ripple
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor, // Gunakan animasi warna
            modifier = Modifier.size(22.dp)
        )
    }
}
