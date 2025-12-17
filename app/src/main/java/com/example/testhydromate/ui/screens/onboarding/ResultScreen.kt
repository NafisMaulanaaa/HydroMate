package com.example.testhydromate.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

    // 🔥 SOURCE OF TRUTH
    val goalValue = viewModel.dailyGoal.toString()

    // TEMP STATE BUAT EDIT
    var tempGoalValue by remember { mutableStateOf("") }

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
@Composable
fun DailyGoalMainScreen(
    modifier: Modifier = Modifier,
    currentGoal: String,
    onAdjustClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Your daily goal is",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.glass_1),
            contentDescription = "Water Glass",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = currentGoal,
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "mL",
                fontSize = 16.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onAdjustClick,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = TextGray
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Adjust",
                fontSize = 13.sp,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun EditGoalSheetContent(
    tempValue: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Edit Your Daily Goal",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(InputBgGray, RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BasicTextField(
                value = tempValue,
                onValueChange = {
                    if (it.length <= 5 && it.all { c -> c.isDigit() }) {
                        onValueChange(it)
                    }
                },
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(IntrinsicSize.Min)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "mL",
                fontSize = 18.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonCancelBg,
                    contentColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(30.dp),
                elevation = null
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        }
    }
}

//@Preview(showBackground = true, widthDp = 390, heightDp = 844)
//Composable
//fun MainDailyGoalPreview() {
//    ResultScreen(
//        onContinueToHome = {}
//    )
//}