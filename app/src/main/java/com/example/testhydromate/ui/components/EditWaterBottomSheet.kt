package com.example.testhydromate.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R
import com.example.testhydromate.data.model.WaterLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWaterBottomSheet(
    log: WaterLog,
    onDismiss: () -> Unit,
    onSave: (WaterLog) -> Unit
) {
    var amountText by remember { mutableStateOf(log.amount.toString()) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* ===== TITLE ===== */
            Text(
                text = "Edit Water Intake",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)

            Spacer(Modifier.height(32.dp))

            /* ===== ICON ===== */
            Image(
                painter = painterResource(id = R.drawable.water),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(Modifier.height(32.dp))

            /* ===== INPUT BOX (Presisi Tengah & Baseline) ===== */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(90.dp)
                    .background(color = Color(0xFFF8F9FA), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = amountText,
                        onValueChange = { if (it.length <= 4) amountText = it.filter { c -> c.isDigit() } },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2),
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .alignByBaseline()
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "mL",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(24.dp))

            /* ===== BUTTONS ===== */
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f).height(56.dp),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD),
                        contentColor = PrimaryBlue
                    ),
                    elevation = null
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }

                Button(
                    modifier = Modifier.weight(1f).height(56.dp),
                    onClick = {
                        val amount = amountText.toIntOrNull()
                        if (amount != null) onSave(log.copy(amount = amount))
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
