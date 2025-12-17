package com.example.testhydromate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import com.example.testhydromate.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.data.model.WaterLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWaterBottomSheet(
    log: WaterLog,
    onDismiss: () -> Unit,
    onSave: (WaterLog) -> Unit
) {
    var amount by remember { mutableStateOf(log.amount.toString()) }

    val dateFormat = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }
    val timeFormat = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Edit Water Intake",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            Icon(
                painter = painterResource(id = R.drawable.water),
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ===== AMOUNT =====
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { c -> c.isDigit() } },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                suffix = { Text("mL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(Modifier.height(20.dp))

            // ===== DATE & TIME (DISPLAY ONLY FOR NOW) =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AssistChip(
                    onClick = { /* next step */ },
                    label = { Text(dateFormat.format(Date(log.timestamp))) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Edit, contentDescription = null)
                    }
                )

                AssistChip(
                    onClick = { /* next step */ },
                    label = { Text(timeFormat.format(Date(log.timestamp))) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Edit, contentDescription = null)
                    }
                )
            }

            Spacer(Modifier.height(28.dp))

            // ===== ACTION BUTTON =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onSave(
                            log.copy(
                                amount = amount.toIntOrNull() ?: log.amount
                            )
                        )
                    }
                ) {
                    Text("Save")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
