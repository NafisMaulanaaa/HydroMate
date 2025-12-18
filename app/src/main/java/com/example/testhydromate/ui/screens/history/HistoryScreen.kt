package com.example.testhydromate.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.components.EditWaterBottomSheet
import com.example.testhydromate.ui.components.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val logs by viewModel.waterLogs.collectAsState()
    val context = LocalContext.current

    var selectedDelete by remember { mutableStateOf<WaterLog?>(null) }
    var selectedEdit by remember { mutableStateOf<WaterLog?>(null) }

    val groupedLogs = logs.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(it.timestamp))
    }

    // ===== MATURE DELETE DIALOG =====
    selectedDelete?.let { log ->
        val time = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))

        ConfirmDeleteDialog(
            time = time,
            onCancel = { selectedDelete = null },
            onConfirm = {
                viewModel.deleteLog(log)
                showDeleteToast(context, time)
                selectedDelete = null
            }
        )
    }

    // ===== EDIT SHEET =====
    selectedEdit?.let { log ->
        EditWaterBottomSheet(
            log = log,
            onDismiss = { selectedEdit = null },
            onSave = {
                viewModel.updateLog(it)
                selectedEdit = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 74.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            groupedLogs.forEach { (date, items) ->
                item {
                    HistoryDateCard(
                        date = date,
                        logs = items,
                        onEdit = { selectedEdit = it },
                        onDelete = { selectedDelete = it }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

/* ================= DATE CARD ================= */

@Composable
fun HistoryDateCard(
    date: String,
    logs: List<WaterLog>,
    onEdit: (WaterLog) -> Unit,
    onDelete: (WaterLog) -> Unit
) {
    Column {

        Text(
            text = formatDateHeader(date),
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                logs.forEachIndexed { index, log ->
                    HistoryItem(
                        log = log,
                        onEdit = onEdit,
                        onDelete = onDelete
                    )

                    if (index < logs.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = Color.LightGray.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

/* ================= ITEM ================= */
@Composable
fun HistoryItem(
    log: WaterLog,
    onEdit: (WaterLog) -> Unit,
    onDelete: (WaterLog) -> Unit
) {
    val time = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))
    }

    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.testhydromate.R.drawable.water),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text("Water", fontWeight = FontWeight.Medium)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }

        Text("${log.amount} mL", fontWeight = FontWeight.Medium)

        Box {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = menuOpen,
                onDismissRequest = { menuOpen = false },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Outlined.Edit, null, tint = Color.Black)
                    },
                    text = { Text("Edit") },
                    onClick = {
                        menuOpen = false
                        onEdit(log)
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Outlined.Delete, null, tint = Color(0xFFD32F2F))
                    },
                    text = {
                        Text(
                            "Delete",
                            color = Color(0xFFD32F2F)
                        )
                    },
                    onClick = {
                        menuOpen = false
                        onDelete(log)
                    }
                )
            }
        }
    }
}

/* ================= MATURE DELETE DIALOG ================= */
@Composable
fun ConfirmDeleteDialog(
    time: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 280.dp, max = 340.dp)
            ) {

                Text(
                    text = "Delete history",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "This action will permanently remove the drink record at $time.",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = Color(0xFF555555))
                    }

                    Spacer(Modifier.width(8.dp))

                    TextButton(onClick = onConfirm) {
                        Text(
                            "Delete",
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/* ================= TOAST ================= */

fun showDeleteToast(context: android.content.Context, time: String) {
    Toast.makeText(
        context,
        "Drink record at $time has been deleted",
        Toast.LENGTH_SHORT
    ).show()
}

/* ================= DATE FORMAT ================= */
fun formatDateHeader(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateObj = sdf.parse(date) ?: return date

    val today = sdf.format(Date())
    val yesterday = sdf.format(
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time
    )

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            .format(dateObj)
    }
}
