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
    onReport: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(400.dp)
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
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val maxWidth = maxWidth
            val itemCount = 4 // DIUBAH JADI 4
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
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp) // Ukuran pill sedikit dikecilkan agar muat 4 item
                        .background(
                            color = PrimaryBlue,
                            shape = RoundedCornerShape(24.dp)
                        )
                )
            }

            // 3. ROW ICON
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
                    icon = Icons.Default.Description, // History
                    selected = selectedIndex == 1,
                    onClick = onHistory,
                    modifier = Modifier.weight(1f)
                )

                // ITEM BARU: REPORT
                BottomItem(
                    icon = Icons.Default.BarChart, // Icon Chart
                    selected = selectedIndex == 2,
                    onClick = onReport,
                    modifier = Modifier.weight(1f)
                )

                BottomItem(
                    icon = Icons.Default.Settings,
                    selected = selectedIndex == 3,
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        label = "scale_anim"
    )

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
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
    }
}