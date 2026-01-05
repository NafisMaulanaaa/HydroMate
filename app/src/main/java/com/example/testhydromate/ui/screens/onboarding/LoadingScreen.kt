package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray
import com.example.testhydromate.util.Resource

@Composable
fun LoadingResultScreen(
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val saveState = viewModel.saveState

    LaunchedEffect(Unit) {
        viewModel.submitOnboarding()
    }

    LaunchedEffect(saveState) {
        when (saveState) {
            is Resource.Success -> {
                progress.animateTo(1f, tween(4500))
                onFinished()
            }
            is Resource.Error -> {
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Generating your plan...",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryBlue
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Please wait...",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress.value,
                    strokeWidth = 18.dp,
                    color = PrimaryBlue,
                    trackColor = Color(0xFFEAEAEA),
                    modifier = Modifier.size(220.dp)
                )

                Text(
                    text = "${(progress.value * 100).toInt()}%",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "This will take a moment. Get Ready to\nTransform your hydration journey!",
                fontSize = 14.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}