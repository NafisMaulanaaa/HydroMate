package com.example.testhydromate.ui.components

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustDrinkAmountBottomSheet(
    initialAmount: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    //  Ambil Context untuk menampilkan Toast
    val context = LocalContext.current

    // Inisialisasi state
    var amountText by remember { mutableStateOf(initialAmount.toString()) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(initialAmount) {
        amountText = initialAmount.toString()
    }

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
            Text(
                text = "Set Drink Amount",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.water),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(Modifier.height(32.dp))

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
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f).height(56.dp),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD),
                        contentColor = PrimaryBlue
                    )
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }

                Button(
                    modifier = Modifier.weight(1f).height(56.dp),
                    onClick = {
                        val finalAmount = amountText.toIntOrNull() ?: 0

                        if (finalAmount > 0) {
                            onSave(finalAmount)
                        } else {
                            // Tampilkan Toast jika jumlahnya 0 atau kosong
                            Toast.makeText(
                                context,
                                "Amount cannot be zero",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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