package com.example.testhydromate.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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

    var selectedLogToDelete by remember { mutableStateOf<WaterLog?>(null) }
    var selectedLogToEdit by remember { mutableStateOf<WaterLog?>(null) }

    val groupedLogs = logs.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(it.timestamp))
    }

    // ===== DELETE DIALOG =====
    selectedLogToDelete?.let { log ->
        val time = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))

        AlertDialog(
            onDismissRequest = { selectedLogToDelete = null },
            title = { Text("Delete history?") },
            text = { Text("Delete drink at $time ?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteLog(log)
                    Toast.makeText(
                        context,
                        "Deleted drink at $time",
                        Toast.LENGTH_SHORT
                    ).show()
                    selectedLogToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedLogToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ===== EDIT BOTTOM SHEET =====
    selectedLogToEdit?.let { log ->
        EditWaterBottomSheet(
            log = log,
            onDismiss = { selectedLogToEdit = null },
            onSave = { updated ->
                viewModel.updateLog(updated)
                selectedLogToEdit = null
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
                "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            groupedLogs.forEach { (date, items) ->
                item {
                    HistoryDateCard(
                        date = date,
                        logs = items,
                        onDeleteClick = { selectedLogToDelete = it },
                        onEditClick = { selectedLogToEdit = it }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun HistoryDateCard(
    date: String,
    logs: List<WaterLog>,
    onDeleteClick: (WaterLog) -> Unit,
    onEditClick: (WaterLog) -> Unit
) {
    Column {

        Text(
            formatDateHeader(date),
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column {
                logs.forEachIndexed { index, log ->
                    HistoryItem(
                        log = log,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick
                    )

                    if (index < logs.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    log: WaterLog,
    onDeleteClick: (WaterLog) -> Unit,
    onEditClick: (WaterLog) -> Unit
) {
    val time = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(log.timestamp))
    }

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, CircleShape)
        )

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text("Water", fontWeight = FontWeight.Medium)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }

        Text("${log.amount} mL", fontWeight = FontWeight.Medium)

        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Default.MoreVert, null)
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Outlined.Edit, null)
                    },
                    text = { Text("Edit") },
                    onClick = {
                        showMenu = false
                        onEditClick(log)
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Outlined.Delete, null, tint = Color.Red)
                    },
                    text = { Text("Delete", color = Color.Red) },
                    onClick = {
                        showMenu = false
                        onDeleteClick(log)
                    }
                )
            }
        }
    }
}

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

