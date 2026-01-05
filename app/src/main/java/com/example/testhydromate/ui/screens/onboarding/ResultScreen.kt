package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.DailyGoalMainScreen
import com.example.testhydromate.ui.components.EditGoalSheetContent
import com.example.testhydromate.ui.components.PrimaryBlue

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