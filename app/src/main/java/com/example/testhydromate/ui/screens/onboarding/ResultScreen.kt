package com.example.testhydromate.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R
import com.example.testhydromate.ui.components.DailyGoalMainScreen
import com.example.testhydromate.ui.components.EditGoalSheetContent
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray
import com.example.testhydromate.ui.screens.onboarding.OnboardingViewModel


private val InputBgGray = Color(0xFFF5F5F5)
private val ButtonCancelBg = Color(0xFFE3F2FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: OnboardingViewModel,
    onContinueToHome: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val goalValue = viewModel.dailyGoal.toString()

    // TEMP STATE BUAT EDIT
    var tempGoalValue by remember { mutableStateOf(goalValue) }

    // Sync temp state saat dailyGoal berubah
    LaunchedEffect(viewModel.dailyGoal) {
        tempGoalValue = viewModel.dailyGoal.toString()
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Button(
                onClick = onContinueToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "Let's hydrate!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        DailyGoalMainScreen(
            modifier = Modifier.padding(innerPadding),
            currentGoal = goalValue,
            onAdjustClick = {
                tempGoalValue = goalValue
                showBottomSheet = true
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            EditGoalSheetContent(
                tempValue = tempGoalValue,
                onValueChange = { tempGoalValue = it },
                onCancel = { showBottomSheet = false },
                onSave = {
                    val newGoal = tempGoalValue.toIntOrNull() ?: return@EditGoalSheetContent
                    viewModel.updateManualGoal(newGoal) // 🔥 UPDATE VIEWMODEL
                    showBottomSheet = false
                }
            )

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// MAIN SCREEN

/*@Preview(showBackground = true, widthDp = 390, heightDp = 844)
Composable
fun MainDailyGoalPreview() {
    ResultScreen(
        onContinueToHome = {}
    )
}*/